const http = require('http');
const fs = require('fs');
const path = require('path');

const root = __dirname;
const PORT = 8765;

const types = {
  '.html': 'text/html; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.gif': 'image/gif',
  '.svg': 'image/svg+xml',
  '.css': 'text/css',
  '.js': 'application/javascript',
  '.txt': 'text/plain; charset=utf-8',
};

http.createServer((req, res) => {
  if (req.method === 'POST' && req.url === '/save') {
    let body = '';
    req.on('data', chunk => body += chunk);
    req.on('end', () => {
      try {
        const { filename, dataUrl } = JSON.parse(body);
        const b64 = dataUrl.split(',')[1];
        const buf = Buffer.from(b64, 'base64');
        const fullPath = path.join(root, 'logo_export', filename);
        fs.mkdirSync(path.dirname(fullPath), { recursive: true });
        fs.writeFileSync(fullPath, buf);
        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ saved: fullPath, size: buf.length }));
      } catch (e) {
        res.writeHead(500, { 'Content-Type': 'text/plain' });
        res.end(String(e));
      }
    });
    return;
  }

  let p = decodeURIComponent(req.url.split('?')[0]);
  if (p === '/' || p === '') p = '/logo_preview.html';
  const file = path.join(root, p);
  if (!file.startsWith(root)) {
    res.writeHead(403); res.end('Forbidden'); return;
  }
  fs.readFile(file, (err, data) => {
    if (err) {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('Not found: ' + p);
      return;
    }
    const ext = path.extname(file).toLowerCase();
    res.writeHead(200, {
      'Content-Type': types[ext] || 'application/octet-stream',
      'Cache-Control': 'no-cache'
    });
    res.end(data);
  });
}).listen(PORT, '127.0.0.1', () => {
  console.log(`Serving ${root} on http://localhost:${PORT}/`);
});
