/*
 * Copyright 2007 Guy Van den Broeck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.outerj.daisy.diff.html.dom;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DomTreeBuilder extends DefaultHandler implements DomTree {

    private List<TextNode> textNodes = new ArrayList<TextNode>(50);

    private BodyNode bodyNode = new BodyNode();

    private TagNode currentParent = bodyNode;

    private StringBuilder newWord = new StringBuilder();

    protected boolean documentStarted = false;

    protected boolean documentEnded = false;

    protected boolean bodyStarted = false;

    protected boolean bodyEnded = false;

    private boolean whiteSpaceBeforeThis = false;

    /**
     * When greater than 0, this indicates that the node being parsed is a descendant of a pre tag.
     */
    private int numberOfActivePreTags = 0; // calculating this as required for every node is expensive.

    private Node lastSibling = null;

    public BodyNode getBodyNode() {
        return bodyNode;
    }

    public List<TextNode> getTextNodes() {
        return textNodes;
    }

    @Override
    public void startDocument() throws SAXException {
        if (documentStarted)
            throw new IllegalStateException(
                    "This Handler only accepts one document");
        documentStarted = true;
    }

    @Override
    public void endDocument() throws SAXException {
        if (!documentStarted || documentEnded)
            throw new IllegalStateException();
        endWord();
        documentEnded = true;
        documentStarted = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        if (!documentStarted || documentEnded)
            throw new IllegalStateException();

        if (bodyStarted && !bodyEnded) {
            endWord();

            TagNode newTagNode = new TagNode(currentParent, localName,
                    attributes);
            currentParent = newTagNode;
            lastSibling = null;
            if (whiteSpaceBeforeThis && newTagNode.isInline()) {
                newTagNode.setWhiteBefore(true);
            }
            whiteSpaceBeforeThis = false;
            if (newTagNode.isPre()) {
                numberOfActivePreTags++;
            }
            if (isSeparatingTag(newTagNode)) {
                addSeparatorNode();
            }

        } else if (bodyStarted) {
            // Ignoring element after body tag closed
        } else if (localName.equalsIgnoreCase("body")) {
            bodyStarted = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (!documentStarted || documentEnded)
            throw new IllegalStateException();

        if (localName.equalsIgnoreCase("body")) {
            bodyEnded = true;
        } else if (bodyStarted && !bodyEnded) {
            if (localName.equalsIgnoreCase("img")) {
                // Insert a dummy leaf for the image
                ImageNode img = new ImageNode(currentParent, currentParent
                        .getAttributes());
                img.setWhiteBefore(whiteSpaceBeforeThis);
                lastSibling = img;
                textNodes.add(img);
            }
            endWord();
            if (currentParent.isInline()) {
                lastSibling = currentParent;
            } else {
                lastSibling = null;
            }
            if (localName.equalsIgnoreCase("pre")) {
                numberOfActivePreTags--;
            }
            if (isSeparatingTag(currentParent)) {
                addSeparatorNode();
            }
            currentParent = currentParent.getParent();
            whiteSpaceBeforeThis = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length)
            throws SAXException {

        if (!documentStarted || documentEnded)
            throw new IllegalStateException();

        for (int i = start; i < start + length; i++) {
            char c = ch[i];
            if (isDelimiter(c)) {
                endWord();
                if (WhiteSpaceNode.isWhiteSpace(c) && numberOfActivePreTags == 0) {
                    if (lastSibling != null)
                        lastSibling.setWhiteAfter(true);
                    whiteSpaceBeforeThis = true;
                } else {
                    TextNode textNode = new TextNode(currentParent, Character
                            .toString(c));
                    textNode.setWhiteBefore(whiteSpaceBeforeThis);
                    whiteSpaceBeforeThis = false;
                    lastSibling = textNode;
                    textNodes.add(textNode);

                }
            } else {
                newWord.append(c);
            }

        }
    }

    private void endWord() {
        if (newWord.length() > 0) {
            TextNode node = new TextNode(currentParent, newWord.toString());
            node.setWhiteBefore(whiteSpaceBeforeThis);
            whiteSpaceBeforeThis = false;
            lastSibling = node;
            textNodes.add(node);
            newWord.setLength(0);
        }
    }

    /**
     * Returns <code>true</code> if the given tag separates text nodes
     * from being successive. I.e. every block starts a new distinct text flow.
     *
     * @param aTagNode
     * @return
     */
    private boolean isSeparatingTag(TagNode aTagNode) {
        // treat all block tags as separating
        return aTagNode.isBlockLevel();
    }

    /**
     * Ensures that a separator is added after the last text node.
     */
    private void addSeparatorNode() {
        if (textNodes.isEmpty()) {
            return;
        }

        // don't add multiple separators
        if (textNodes.get(textNodes.size() - 1) instanceof SeparatingNode) {
            return;
        }

        textNodes.add(new SeparatingNode(currentParent));
    }

    public static boolean isDelimiter(char c) {
        if (WhiteSpaceNode.isWhiteSpace(c))
            return true;
        switch (c) {
            // Basic Delimiters
            case '/':
            case '.':
            case '!':
            case ',':
            case ';':
            case '?':
            case '=':
            case '\'':
            case '"':
                // Extra Delimiters
            case '[':
            case ']':
            case '{':
            case '}':
            case '(':
            case ')':
            case '&':
            case '|':
            case '\\':
            case '-':
            case '_':
            case '+':
            case '*':
            case ':':
                return true;
            default:
                return false;
        }
    }

}
