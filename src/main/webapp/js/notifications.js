function showModal(message) {
    const modal = document.getElementById('notificationModal');
    document.getElementById('modalMessage').textContent = message;
    modal.style.display = 'block';

    document.querySelector('.modalClose').onclick = () => modal.style.display = 'none';
    document.getElementById('modalOk').onclick = () => modal.style.display = 'none';
}

function showToast(message, type) {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;

    document.body.appendChild(toast);
    requestAnimationFrame(() => toast.classList.add('show'));

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 500);
    }, 2000);
}
