CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE t_accounts (
    t_acc_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    t_acc_number VARCHAR(50) NOT NULL UNIQUE,
    t_acc_currency VARCHAR(3) NOT NULL, -- VND, USD, EUR, etc.
    t_acc_balance DECIMAL(19,4) NOT NULL DEFAULT 0,
    t_acc_state VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, FROZEN, CLOSED
    t_acc_created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    t_acc_updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    t_acc_version BIGINT DEFAULT 0 -- For optimistic locking
);

CREATE TABLE t_transactions (
    t_trans_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    t_trans_reference_id VARCHAR(100) NOT NULL, -- client reference id for idempotency,
    t_trans_booking_date TIMESTAMP WITH TIME ZONE NOT NULL,
    t_trans_description TEXT,
    t_trans_metadata JSONB
);

CREATE TABLE t_ledger_entries (
  t_led_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  t_led_transaction_id UUID NOT NULL REFERENCES t_transactions(t_trans_id),
  t_led_account_id UUID NOT NULL REFERENCES t_accounts(t_acc_id),
  t_led_type VARCHAR(10) NOT NULL, -- DEBIT / CREDIT
  t_led_amount DECIMAL(19, 4) NOT NULL CHECK (t_led_amount > 0),
  t_led_created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_txn_reference ON t_transactions(t_trans_reference_id);
CREATE INDEX idx_entries_account ON t_ledger_entries(t_led_account_id);