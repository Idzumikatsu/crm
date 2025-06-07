# DNS Zone Configuration

This directory contains BIND zone files for `english.school`. The zone records
mirror the configuration described in `docs/DNS_SETUP.md`.

Update the `SOA` serial number each time records change.
Deploy using your DNS provider's control panel or via `nsupdate` if using BIND.
