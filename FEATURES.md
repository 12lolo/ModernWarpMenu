# Features Added

## Visual/Layout Changes
- Added a new **Rift NPC portal** entry to the overworld fast-travel layout.
- Added and integrated texture asset:
  - `src/main/resources/assets/modernwarpmenu/textures/gui/islands/rift_npc.png`

## Layout Editor Tool
- Added a standalone visual editor:
  - `tools/layout-editor.html`
- Supports:
  - Dragging islands on the 64x36 screen grid.
  - Dragging warp portals on island-local grid.
  - Undo/Redo.
  - Editor mode vs Preview mode.
  - Runtime-like hover simulation in Preview.
  - Desktop resolution presets and manual resize.
  - Zoom controls (including Ctrl+wheel pointer-focused zoom).
  - Reserved settings-button area to prevent accidental overlap.
  - Load from repo `layout.json`.
  - Save to repo `layout.json` with destructive overwrite confirmation.
  - JSON download/export as a separate action.

## Local Save Server
- Added local editor server:
  - `tools/layout-editor-server.ps1`
  - `tools/layout-editor-server.py`
  - `tools/start-layout-editor.sh`
- Provides:
  - Static hosting for the editor (default: `http://localhost:8756/tools/layout-editor.html`).
  - Direct overwrite endpoint `POST /api/save-layout` for:
    - `src/main/generated/assets/modernwarpmenu/layouts/layout.json`

## Quick Setup (User)
- Linux (Arch btw🙃):
  - `chmod +x ./tools/start-layout-editor.sh`
  - `./tools/start-layout-editor.sh`
- Windows (PowerShell):
  - `.\tools\layout-editor-server.ps1`
- Open in browser:
  - Use the exact URL printed by the server in terminal output.
  - If no custom port was provided, use `http://localhost:8756/tools/layout-editor.html`.
- Save actions:
  - `Save To Repo layout.json`: overwrites repo `layout.json` directly (with confirmation).
  - `Download JSON`: downloads an exported copy.
- If PowerShell scripts are blocked on Windows, run once:
  - `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned`

## Cleanup
- Removed old preview guide (`PREVIEW_GUIDE.md`).
