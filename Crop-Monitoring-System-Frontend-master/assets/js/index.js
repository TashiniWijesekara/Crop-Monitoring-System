$(document).ready(function () {
  $("#showSignUp").on("click", function () {
    $(".form-card").addClass("sign-up-mode");
  });

  $("#showSignIn").on("click", function () {
    $(".form-card").removeClass("sign-up-mode");
  });
});

$("#signInForm").on("submit", function (e) {
  e.preventDefault();

  const email = $("#signInEmail").val();
  const password = $("#signInPassword").val();

  $.ajax({
    url: "http://localhost:5050/cropmonitoring/api/v0/auth/signin",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify({ email, password }),
    success: function (response) {
      // Save tokens and role in localStorage
      localStorage.setItem("accessToken", response.token);
      localStorage.setItem("userRole", response.role); // Store user role
      
      // Redirect to dashboard
      window.location.href = "/sidebar.html";
    },
    error: function (xhr) {
      alert("Login failed: " + xhr.responseText);
    },
  });
});

$("#signUpForm").on("submit", function (e) {
  e.preventDefault();

  const email = $("#signUpEmail").val();
  const password = $("#signUpPassword").val();
  const role = $("select").val();

  $.ajax({
    url: "http://localhost:5050/cropmonitoring/api/v0/auth/signup",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify({ email, password, role }),
    success: function (response) {
      alert("Sign Up successful! Please log in.");
      $("#showSignIn").click();
    },
    error: function (xhr) {
      alert("Sign Up failed: " + xhr.responseText);
    },
  });
});