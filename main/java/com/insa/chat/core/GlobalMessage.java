/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.insa.chat.core;

/**
 *
 * @author polo
 */
public class GlobalMessage {
    
    private String type;
    private String data;
    
    public GlobalMessage(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }
    
}
