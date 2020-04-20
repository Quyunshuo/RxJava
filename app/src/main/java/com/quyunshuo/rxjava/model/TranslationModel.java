package com.quyunshuo.rxjava.model;

import com.google.gson.annotations.SerializedName;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/20
 * @Class: TranslationModel
 * @Remark: 请求翻译的结果
 */
public class TranslationModel {

    /**
     * status : 1
     * content : {"from":"en-EU","to":"zh-CN","vendor":"wps","out":" 你好，世界","ciba_use":"来自机器翻译。","ciba_out":"","err_no":0}
     */

    @SerializedName("status")
    private int status;
    @SerializedName("content")
    private ContentBean content;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TranslationModel{" +
                "status=" + status +
                ", content=" + content +
                '}';
    }

    public static class ContentBean {
        /**
         * from : en-EU
         * to : zh-CN
         * vendor : wps
         * out :  你好，世界
         * ciba_use : 来自机器翻译。
         * ciba_out :
         * err_no : 0
         */

        @SerializedName("from")
        private String from;
        @SerializedName("to")
        private String to;
        @SerializedName("vendor")
        private String vendor;
        @SerializedName("out")
        private String out;
        @SerializedName("ciba_use")
        private String cibaUse;
        @SerializedName("ciba_out")
        private String cibaOut;
        @SerializedName("err_no")
        private int errNo;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getOut() {
            return out;
        }

        public void setOut(String out) {
            this.out = out;
        }

        public String getCibaUse() {
            return cibaUse;
        }

        public void setCibaUse(String cibaUse) {
            this.cibaUse = cibaUse;
        }

        public String getCibaOut() {
            return cibaOut;
        }

        public void setCibaOut(String cibaOut) {
            this.cibaOut = cibaOut;
        }

        public int getErrNo() {
            return errNo;
        }

        public void setErrNo(int errNo) {
            this.errNo = errNo;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", vendor='" + vendor + '\'' +
                    ", out='" + out + '\'' +
                    ", cibaUse='" + cibaUse + '\'' +
                    ", cibaOut='" + cibaOut + '\'' +
                    ", errNo=" + errNo +
                    '}';
        }
    }
}
