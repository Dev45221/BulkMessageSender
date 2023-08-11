package com.example.automessagessender;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class WhatAppAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (getRootInActiveWindow() == null) {
            return;
        }

        ///getting root node
        AccessibilityNodeInfoCompat rootNodeInfo = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());

        //get edit text if message from whatsApp
        List<AccessibilityNodeInfoCompat> messageNodeList = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
        if (messageNodeList == null || messageNodeList.isEmpty())
            return;

        //check if the message field is filled with the text and enfding with our suffix
        AccessibilityNodeInfoCompat messageField = messageNodeList.get(0);
        if (messageField == null || messageField.getText().length() == 0 || !messageField.getText().toString().endsWith("   "))
            return;

        //get whatsapp send message button node list
        List<AccessibilityNodeInfoCompat> sendMessageNodeList = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
        if (sendMessageNodeList == null || sendMessageNodeList.isEmpty())
            return;

        AccessibilityNodeInfoCompat sendMessage = sendMessageNodeList.get(0);
        if (!sendMessage.isVisibleToUser())
            return;

        //fire and send
        sendMessage.performAction(AccessibilityNodeInfo.ACTION_CLICK);

        //go back to our app by clicking back button twice
        try{
            Thread.sleep(2000);
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(2000);
        }catch (InterruptedException ignored){}

        performGlobalAction(GLOBAL_ACTION_BACK);

    }

    @Override
    public void onInterrupt() {

    }
}
