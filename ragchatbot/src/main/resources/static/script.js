async function uploadFile() {
    const fileInput = document.getElementById('fileInput');
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    const response = await fetch('/api/upload/file', {
        method: 'POST',
        body: formData
    });

    alert(await response.text());
}

async function uploadText() {
    const textInput = document.getElementById('textInput').value;

    const response = await fetch('/api/upload/text', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ text: textInput })
    });

    alert(await response.text());
}

async function sendQuery() {
    const queryInput = document.getElementById('queryInput').value;

    const response = await fetch('/api/chat/query', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: queryInput })
    });

    document.getElementById('response').innerText = await response.text();
}
