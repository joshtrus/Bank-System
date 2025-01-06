Bank Software System

Overview

This project implements a bank software system that processes a series of commands provided as a list of strings. The commands allow for the creation and management of different types of bank accounts, as well as various operations such as deposits, withdrawals, transfers, and account balance updates. The system validates all inputs and outputs the results as a list of strings, with no user interaction (e.g., no console input/output).

Features

Account Types: Supports checking, savings, and certificate of deposit (CD) accounts.

Account Creation: Accounts are created with unique identifiers and specified APRs.

Transactions:

Deposits

Withdrawals

Transfers

Time Passing: Processes APR accrual and account maintenance fees for specified time periods.

Validation: Ensures all commands are well-formed and rejects invalid commands with appropriate feedback.

Output: Provides formatted account states and transaction histories.

Commands

Account Creation

Syntax:

create <account type> <id> <apr>

create cd <id> <apr> <amount>

Rules:

Account types: checking, savings, cd

id: Unique 8-digit number

apr: Percentage between 0 and 10 (e.g., 0.06, 3, 9.8)

CDs require an additional initial amount ($1000 to $10000)

Deposit

Syntax: deposit <id> <amount>

Rules:

Maximum deposit: $1000 (checking), $2500 (savings)

CDs do not support deposits.

Withdrawal

Syntax: withdraw <id> <amount>

Rules:

Maximum withdrawal: $400 (checking), $1000 (savings)

CDs can only withdraw the full balance after 12 months.

Transfer

Syntax: transfer <from id> <to id> <amount>

Rules:

Only between checking and savings accounts.

CDs are excluded from transfers.

Pass Time

Syntax: pass <months>

Rules:

Valid range: 1 to 60 months.

Processes APR accrual, maintenance fees, and account closures.

Output

Account State: Includes account type, ID, balance, and APR.

Transaction History: Lists all valid commands affecting the account (excluding pass commands).

Invalid Commands: Outputs all invalid commands in the order they were encountered.

Formatting:

Balances and APR values are truncated to two decimal places.

Example

Input

create savings 12345678 0.6
create cd 87654321 2.1 500
create cd 87654321 1.2 2000
deposit 12345678 1500
withdraw 12345678 500
pass 12

Output

savings 12345678 1000.00 0.60
deposit 12345678 1500
withdraw 12345678 500
cd 87654321 2014.03 2.10
invalid: create cd 87654321 2.1 500

Development Workflow

Implement commands incrementally, following the provided rules and validation criteria.

Write comprehensive unit tests for each command type and edge case.

Use Test-Driven Development (TDD) principles to ensure correctness.

Maintain modular and readable code to facilitate debugging and enhancements.

Technologies

Language: Java

Testing: JUnit

Contribution Guidelines

Fork the repository and create a feature branch.

Ensure all code is covered by unit tests.

Follow consistent naming conventions and formatting.

Submit a pull request with a clear description of changes.

License

This project is licensed under the MIT License. See the LICENSE file for details.

