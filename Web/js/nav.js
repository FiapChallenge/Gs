const toggleBtn = document.querySelector('.toggle-btn');
const toggleBtnIcon = document.querySelector('.toggle-btn i');
const dropDownMenu = document.querySelector('.dropdown-menu');

let isOpen = false;

toggleBtn.onclick = function () {
  dropDownMenu.classList.toggle('open');
  isOpen = dropDownMenu.classList.contains('open');

  toggleBtnIcon.classList = isOpen ? 'fa-solid fa-xmark' : 'fa solid fa-bars';
};

let prevScrollpos = window.scrollY;

window.onscroll = function () {
  let currentScrollPos = window.scrollY;
  console.log(currentScrollPos);
  if (prevScrollpos > currentScrollPos || currentScrollPos < 64 || isOpen) {
    document.getElementById("navbar").style.transform = "translateY(0)";
  } else {
    document.getElementById("navbar").style.transform = "translateY(-100%)";
  }
  prevScrollpos = currentScrollPos;
};