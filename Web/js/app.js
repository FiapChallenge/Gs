const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
        console.log(entry);
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