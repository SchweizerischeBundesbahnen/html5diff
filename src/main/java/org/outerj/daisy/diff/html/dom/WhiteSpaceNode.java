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

import org.outerj.daisy.diff.html.modification.Modification;

public class WhiteSpaceNode extends TextNode {

    public WhiteSpaceNode(TagNode parent, String s) {
        super(parent, s);
    }

    public WhiteSpaceNode(TagNode parent, String s, Node like) {
        this(parent, s);

        try {
            TextNode textNode = (TextNode) like;
            Modification newModification = textNode.getModification().clone();

            newModification.setFirstOfID(false);
            setModification(newModification);

        } catch (ClassCastException e) {
        } catch (NullPointerException e) {
        }
    }

    public static boolean isWhiteSpace(char c) {
        switch (c) {
            case ' ':
            case '\t':
            case '\r':
            case '\n':
                return true;
            default:
                return false;
        }
    }

}
