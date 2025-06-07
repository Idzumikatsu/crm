ALTER TABLE notification_template
    ADD CONSTRAINT notification_template_code_lang_uq UNIQUE (code, lang);
