# SafeGuard Browser Filter (Android)

This project is a **starter Android app** for device-wide adult-content filtering using `VpnService`.

## What it does
- Starts a local VPN tunnel to inspect network metadata.
- Blocks a sample set of known adult domains.
- Detects explicit search queries.
- Enforces strict safe-search query params for major search engines.

## Important limitations
No app can realistically guarantee blocking **all possible** adult websites. New domains, encrypted DNS,
VPN/proxy tools, mirrors, and app-level traffic behavior make perfect blocking impossible.

To improve real-world coverage, combine:
1. VPN-level DNS/SNI filtering.
2. Continuously updated threat/content feeds.
3. OS-level parental controls (Google Family Link / device policy).
4. Optional browser policy management on managed devices.

## Next steps
- Implement packet I/O loop for DNS/HTTP filtering in `ContentFilterVpnService`.
- Add remote-synced blocklists.
- Add tamper protections (device admin / MDM profile for enterprise).
- Add local audit log and transparency controls.
