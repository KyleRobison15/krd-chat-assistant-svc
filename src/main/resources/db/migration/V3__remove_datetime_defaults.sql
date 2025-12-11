-- Remove DEFAULT CURRENT_TIMESTAMP from chat table
-- Hibernate manages timestamps via @CreationTimestamp and @UpdateTimestamp
ALTER TABLE chat
    MODIFY COLUMN created_at DATETIME NOT NULL,
    MODIFY COLUMN updated_at DATETIME NOT NULL;

-- Remove DEFAULT CURRENT_TIMESTAMP from chat_messages table
-- Hibernate manages timestamp via @CreationTimestamp
ALTER TABLE chat_messages
    MODIFY COLUMN created_at DATETIME NOT NULL;
