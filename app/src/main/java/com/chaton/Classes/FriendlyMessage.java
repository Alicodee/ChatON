/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chaton.Classes;

import android.net.Uri;

public class FriendlyMessage {

    private String text;
    private String name;
    private String photoUrl;
    private String emailNode;
    private String dp;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public FriendlyMessage() {
    }

    public String getEmailNode() {
        return emailNode;
    }

    public void setEmailNode(String emailNode) {
        this.emailNode = emailNode;
    }

    public FriendlyMessage(String time,String text, String name, String photoUrl, String emailNode,String uri) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.emailNode = emailNode;
        this.dp =uri;
        this.time= time;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
