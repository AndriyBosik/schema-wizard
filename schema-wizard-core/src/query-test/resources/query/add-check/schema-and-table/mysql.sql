ALTER TABLE schemawizard.users ADD CONSTRAINT chk_users_age_greater_than_zero CHECK (age > 0)