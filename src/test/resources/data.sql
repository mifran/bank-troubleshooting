INSERT INTO ACCOUNT(ID, NAME, TYPE, OWNER) VALUES (1001, 'Jeremy', 'SAVINGS', 'sarah1');
INSERT INTO ACCOUNT(ID, NAME, TYPE, OWNER) VALUES (1002, 'Emma', 'SAVINGS', 'sarah1');
INSERT INTO ACCOUNT(ID, NAME, TYPE, OWNER) VALUES (1003, 'Sally', 'SAVINGS', 'sarah1');
INSERT INTO ACCOUNT(ID, NAME, TYPE, OWNER) VALUES (1004, 'John', 'SAVINGS', 'sarah1');
INSERT INTO ACCOUNT(ID, NAME, TYPE, OWNER) VALUES (1005, 'Emma', 'MONEY_MARKET', 'sarah1');
INSERT INTO ACCOUNT(ID, NAME, TYPE, OWNER) VALUES (1006, 'Emma', 'CHECKING', 'sarah1');

INSERT INTO JOURNAL_ENTRY(ID, ACCOUNT, DESCRIPTION, INSTANT, AMOUNT, BALANCE) VALUES (13892, 1001, 'Beginning Balance', 16765, '0.00', '100.00');
INSERT INTO JOURNAL_ENTRY(ID, ACCOUNT, DESCRIPTION, INSTANT, AMOUNT, BALANCE) VALUES (13893, 1002, 'Beginning Balance', 16765, '0.00', '0.00');
INSERT INTO JOURNAL_ENTRY(ID, ACCOUNT, DESCRIPTION, INSTANT, AMOUNT, BALANCE) VALUES (13894, 1002, 'Deposit', 16765, '125.00', '125.00');
INSERT INTO JOURNAL_ENTRY(ID, ACCOUNT, DESCRIPTION, INSTANT, AMOUNT, BALANCE) VALUES (13895, 1002, 'Withdrawal', 16765, '-20.00', '105.00');
INSERT INTO JOURNAL_ENTRY(ID, ACCOUNT, DESCRIPTION, INSTANT, AMOUNT, BALANCE) VALUES (13896, 1002, 'Deposit', 16765, '80.00', '185.00');
INSERT INTO JOURNAL_ENTRY(ID, ACCOUNT, DESCRIPTION, INSTANT, AMOUNT, BALANCE) VALUES (13897, 1002, 'Interest payment', 16765, '1.00', '186.00');

