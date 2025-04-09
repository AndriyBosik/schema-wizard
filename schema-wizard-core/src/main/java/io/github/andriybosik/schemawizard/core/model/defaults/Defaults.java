package io.github.andriybosik.schemawizard.core.model.defaults;

public class Defaults {
    private final Text text;
    public Defaults(Text text){
        this.text = text;
    }

    public Text getText() {
        return text;
    }

    public static DefaultsBuilder builder(){
        return new DefaultsBuilder();
    }
    public static class DefaultsBuilder {
        private Text text;

        private DefaultsBuilder(){
        }

        public DefaultsBuilder text(Text text){
            this.text = text;
            return this;
        }

        public Defaults build(){
            return new Defaults(text);
        }
    }
}
