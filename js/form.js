const form = document.querySelector('#message-form');
const submitBtn = document.querySelector('.submit-btn');

function validateForm() {
    var fname = document.getElementById("fname").value;
    var email = document.getElementById("email").value;
    var phone = document.getElementById("phone").value;
    var interest = document.getElementById("interest").value;
    var subject = document.getElementById("subject").value;
    var message = document.getElementById("message").value;
    var source = document.getElementById("source").value;
    var privacy = document.getElementById("privacy").checked;

    if (!fname || !email || !phone || !interest || !subject || !message || !source || !privacy) {
        alert("Por favor, preencha todos os campos obrigatórios.");
        return false;
    }

    return true;
}

const inputs = document.querySelectorAll("input, select, textarea");

inputs.forEach(input => {
  input.addEventListener(
    "invalid",
    () => {
      input.classList.add("error");
      input.addEventListener(
        "input",
        () => {
            if (input.validity.valid) {
                input.classList.remove("error");
            }
            },
            false
        );
    },
    false
  );
});
