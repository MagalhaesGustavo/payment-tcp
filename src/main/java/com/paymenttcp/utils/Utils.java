package com.paymenttcp.utils;

import com.paymenttcp.enums.TagsEnum;
import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlv;
import com.payneteasy.tlv.BerTlvs;

public class Utils {

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static String extractTagValue(BerTlvs berTlvs, TagsEnum tagEnum) {
        BerTlv berTlv = berTlvs.find(new BerTag(tagEnum.getTagBytes()));

        if (berTlv == null) {
            throw new IllegalArgumentException("Tag not found: " + tagEnum.name());
        }
        return berTlv.getHexValue();
    }
}
