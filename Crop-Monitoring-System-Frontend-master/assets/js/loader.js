document.addEventListener("DOMContentLoaded", function () {
  const loader = document.getElementById("page-loader");
  const mainContent = document.getElementById("content"); // Updated to use "content"

  setTimeout(() => {
    if (loader) loader.style.display = "none";
    if (mainContent) mainContent.style.display = "block";
  }, 1000);
});
