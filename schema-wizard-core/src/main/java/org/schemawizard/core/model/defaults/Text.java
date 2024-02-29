package org.schemawizard.core.model.defaults;


public class Text {
    private final Integer defaultLength;

    public Text(Integer defaultLength){
        this.defaultLength = defaultLength;
    }

    public Integer getDefaultLength() {
        return defaultLength;
    }

    public static TextBuilder builder() {
        return new TextBuilder();
    }


    public static class TextBuilder {
        private Integer defaultLength;

        private TextBuilder() {
        }

        public TextBuilder defaultLength(Integer defaultLength) {
            this.defaultLength = defaultLength;
            return this;
        }

        public Text build() {
            return new Text(defaultLength);
        }
    }
}


//public Text build() {
//    return new Text(defaultLength);
//}