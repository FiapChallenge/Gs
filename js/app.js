const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('show');
        } else {
            entry.target.classList.remove('show');
        }
    });
});

const hiddenElements = document.querySelectorAll('.hidden');
if (window.innerWidth > 768) {
    hiddenElements.forEach(hiddenElement => observer.observe(hiddenElement));
}

// remove the class 'hidden' from the elements if window is > 768px
if (window.innerWidth < 768) {
    hiddenElements.forEach(hiddenElement => hiddenElement.classList.remove('hidden'));
}

document.addEventListener('DOMContentLoaded', function () {
    var arrowElement = document.querySelector('.scroll-down-arrow');

    arrowElement.addEventListener('click', function () {
        window.scrollTo({
            top: window.innerHeight + 24,
            behavior: 'smooth'
        });
    });
});