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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.outerj.daisy.diff.html.dom.helper.LastCommonParentResult;
import org.xml.sax.Attributes;

/**
 * Represents any element in the DOM tree of a HTML file.
 */
public abstract class Node {

    private static final String SPAN_TAG = "span";
    private static final String ANCHOR_TAG = "a";

    protected TagNode parent;
    private TagNode root;

    /**
     * This constructor not only sets the parameter as the parent for the
     * created node, but also appends the created node to the collection
     * of the parent's children.
     *
     * @param parent - the parent for the new node.
     */
    public Node(TagNode parent) {
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
            this.root = parent.getRoot();
        } else if (this instanceof TagNode) {
            this.root = (TagNode) this;
        }

    }

    /**
     * @return the parent itself (NOT a copy!)
     */
    public TagNode getParent() {
        return parent;
    }

    /**
     * This method returns a list of the ancestors
     * that is ordered starting from the root by the depth.
     * Index of an element in that list corresponds its depth
     * (if depth of the root is 0)
     *
     * @return ordered by depth list of the ancestors or an empty
     * <code>List&lt;TagNode></code> if the parent is null.
     */
    public List<TagNode> getParentTree() {
        List<TagNode> ancestors = new ArrayList<TagNode>();
        for (TagNode ancestor = getParent(); ancestor != null; ancestor = ancestor.getParent()) {
            ancestors.add(ancestor);
        }
        Collections.reverse(ancestors);
        return ancestors;
    }

    //change for correct insertion of the deleted nodes

    /**
     * "equals" method should work differently for
     * the case where the compared nodes are from the same tree,
     * and in that case return true only if it's the same object
     * This method returns the root of the tree (which should be
     * common ancestor for every node in the tree). If the roots
     * are the same object, then the nodes are in the same tree.
     *
     * @return the "top" ancestor if this node has a parent,<br>
     * or<br>
     * the node itself if there is no parent,
     * and this is a <code>TagNode</code><br>
     * or<br>
     * null if there is no parents and this node isn't a <code>TagNode</code>
     */
    public TagNode getRoot() {
        return root;
    }

    public abstract List<Node> getMinimalDeletedSet(long id);

    public void detectIgnorableWhiteSpace() {
        // no op
    }

    /**
     * Descent the ancestors list for both nodes stopping either
     * at the first no-match case or when either of the lists is exhausted.
     *
     * @param other - the node to check for common parent
     * @return result that contains last common parent, depth,
     * index in the list of children of the common parent of
     * an ancestor(or self) of this node that is
     * immediate child of the common parent.
     * @throws java.lang.IllegalArgumentException if the parameter is null
     */
    public LastCommonParentResult getLastCommonParent(Node other) {
        if (other == null)
            throw new IllegalArgumentException("The given TextNode is null");

        LastCommonParentResult result = new LastCommonParentResult();

        //note that these lists are never null,
        //but sometimes are empty.
        List<TagNode> myParents = getParentTree();
        List<TagNode> otherParents = other.getParentTree();

        int i = 1;
        boolean isSame = true;
        while (isSame && i < myParents.size() && i < otherParents.size()) {
            if (!myParents.get(i).isSameTag(otherParents.get(i))) {
                isSame = false;
            } else {
                // After the while, the index i-1 must be the last common parent
                i++;
            }
        }

        result.setLastCommonParentDepth(i - 1);
        result.setLastCommonParent(myParents.get(i - 1));

        if (!isSame) {//found different parent
            result.setIndexInLastCommonParent(
                    myParents.get(i - 1).getIndexOf(myParents.get(i)));
            result.setSplittingNeeded();
        } else if (myParents.size() < otherParents.size()) {
            //current node is not so deeply nested
            result.setIndexInLastCommonParent(
                    myParents.get(i - 1).getIndexOf(this));
        } else if (myParents.size() > otherParents.size()) {
            // All tags matched but there are tags left in this tree - 
            //other node is not so deeply nested
            result.setIndexInLastCommonParent(
                    myParents.get(i - 1).getIndexOf(myParents.get(i)));
            result.setSplittingNeeded();
        } else {
            // All tags matched until the very last one in both trees
            // or there were no tags besides the BODY
            result.setIndexInLastCommonParent(
                    myParents.get(i - 1).getIndexOf(this));
        }
        return result;
    }

    /**
     * changes the parent field of this node. Does NOT append/remove
     * itself from the previous or the new parent children collection.
     *
     * @param parent - new parent to assign
     */
    public void setParent(TagNode parent) {
        this.parent = parent;
        if (parent != null)
            setRoot(parent.getRoot());
    }

    protected void setRoot(TagNode root) {
        this.root = root;
    }

    public abstract Node copyTree();

    /**
     * @return <code>true</code> only if one of the ancestors is
     * &lt;pre> tag. <code>false</code> otherwise (including case
     * where this node is &lt;pre> tag)
     */
    public boolean inPre() {
        for (TagNode ancestor : getParentTree()) {
            if (ancestor.isPre()) {
                return true;
            }
        }
        return false;
    }

    private boolean whiteBefore = false;

    private boolean whiteAfter = false;

    public boolean isWhiteBefore() {
        return whiteBefore;
    }

    public void setWhiteBefore(boolean whiteBefore) {
        this.whiteBefore = whiteBefore;
    }

    public boolean isWhiteAfter() {

        return whiteAfter;
    }

    public void setWhiteAfter(boolean whiteAfter) {
        this.whiteAfter = whiteAfter;
    }

    public abstract Node getLeftMostChild();

    public abstract Node getRightMostChild();

    protected TagNode getEnclosingPolarionRteLink(TagNode node) {
        if (node == null) {
            return null;
        } else if (isPolarionRteLink(node)) {
            return node;
        } else {
            TagNode parent = node.getParent();
            if (parent != null && (SPAN_TAG.equals(parent.getQName()) || ANCHOR_TAG.equals(parent.getQName()))) {
                return getEnclosingPolarionRteLink(parent);
            } else {
                return null; // If parent node is not span or anchor - stop navigating, because it's not a Polarion RTE link subtree
            }
        }
    }

    protected boolean isPolarionRteLink(TagNode node) {
        return SPAN_TAG.equals(node.getQName()) && "polarion-rte-link".equals(node.getAttributes().getValue("class"));
    }

    protected boolean pairedLinks(TagNode linkA, TagNode linkB) {
        String optionId = getOptionId(linkA.getAttributes());
        if (Objects.equals(optionId, getOptionId(linkB.getAttributes()))
                && (Objects.equals(getItemId(linkA.getAttributes()), getItemId(linkB.getAttributes())) // Items with same IDs also supposed as paired
                        || Objects.equals(getItemId(linkA.getAttributes()), getPairedItemId(linkB.getAttributes()))
                        || Objects.equals(getItemId(linkB.getAttributes()), getPairedItemId(linkA.getAttributes())))) {
            if ("custom".equals(optionId)) {
                Node childNodeA = linkA.getNbChildren() == 1 ? linkA.getChild(0) : null;
                Node childNodeB = linkB.getNbChildren() == 1 ? linkB.getChild(0) : null;
                if (childNodeA instanceof TagNode && childNodeB instanceof TagNode) {
                    TagNode childTagNodeA = (TagNode) childNodeA;
                    TagNode childTagNodeB = (TagNode) childNodeB;
                    return getInnerText(childTagNodeA).equals(getInnerText(childTagNodeB));
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private String getInnerText(TagNode node) {
        return StreamSupport.stream(node.spliterator(), false)
                .filter(n -> n instanceof TextNode)
                .map(n -> ((TextNode) n).getText())
                .collect(Collectors.joining(" "));
    }

    protected String getItemId(Attributes attributes) {
        return attributes.getValue("data-item-id");
    }

    protected String getPairedItemId(Attributes attributes) {
        return attributes.getValue("data-paired-item-id");
    }

    protected String getOptionId(Attributes attributes) {
        return attributes.getValue("data-option-id");
    }

}
