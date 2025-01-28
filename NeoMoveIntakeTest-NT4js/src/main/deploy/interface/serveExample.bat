cd %~dp0\..
start "" http://localhost:5800/interface/outlineViewer.html
python -m http.server -b 127.0.0.1 5810
pause