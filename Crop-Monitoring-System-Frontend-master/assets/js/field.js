$(document).ready(function () {
  $("#crops").select2({
    placeholder: "Select crops",
    allowClear: true,
    closeOnSelect: false,
    width: "100%",
  });
});

// field code genarate

function generateFieldCode() {
  const code = "F-" + Math.floor(1000 + Math.random() * 900);
  document.getElementById("fieldCode").value = code;
}

// Save Field
$("#fieldForm").on("submit", function (e) {
  e.preventDefault();

  let formData = new FormData(this);
  formData.append("fieldCode", $("#fieldCode").val());
  formData.append("fieldName", $("#fieldName").val());
  formData.append("fieldLocation", $("#fieldLocation").val());
  formData.append("extentSize", $("#fieldSize").val());
  formData.append("fieldImage1", $("#fieldImage1")[0].files[0]);
  formData.append("fieldImage2", $("#fieldImage2")[0].files[0]);

  $.ajax({
    url: "http://localhost:5050/cropmonitoring/api/v1/fields",
    type: "POST",
    data: formData,
    contentType: false,
    processData: false,
    headers: {
      Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
    },
    success: function (response) {
      alert("Field saved successfully!");
      $("#fieldForm")[0].reset();
      generateFieldCode();
    },

    error: function (xhr) {
      if (xhr.status === 401) {
        // Handle session expiration
        if (confirm("Session expired. Please log in again.")) {
          window.location.href = "/index.html";
        }
      } else if (xhr.status === 403) {
        // Handle insufficient permissions
        alert("You do not have permission to perform this action.");
      } else {
        // Handle other errors
        alert("Error saving field: " + (xhr.responseText || "An unexpected error occurred."));
      }
    },
  });
});

// Get All Fields
$("#getAllBtn").on("click", function () {
  $.ajax({
    url: "http://localhost:5050/cropmonitoring/api/v1/fields/allFields",
    type: "GET",
    contentType: "application/json",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
    },
    success: function (data) {
      let tableBody = $("#fieldTableBody");
      tableBody.empty();
      data.forEach((field) => {
        tableBody.append(`
                  <tr>
                      <td>${field.fieldCode}</td>
                      <td>${field.fieldName}</td>
                      <td>${field.fieldLocation}</td>
                      <td>${field.extentSize}</td>
                      <td><img src="data:image/png;base64,${field.fieldImage1}" alt="Image 1" style="max-height: 50px;"></td>
                      <td><img src="data:image/png;base64,${field.fieldImage2}" alt="Image 2" style="max-height: 50px;"></td>
                  </tr>
              `);
      });
      $("#fieldListModal").modal("show");
    },
    error: function (xhr) {
      if (xhr.status === 401)
          // Handle session expiration
        if (confirm("Session expired. Please log in again.")) {
          window.location.href = "/index.html";
        }
        else {
          // Handle other errors
          alert("Error retrieving field list : " + (xhr.responseText || "An unexpected error occurred."));
        }
    },
  });
});

// Clear Field
$(document).ready(function () {
  generateFieldCode();

  $("#clearBtn").on("click", function () {
    generateFieldCode();

    $("#fieldForm")
        .find("input, select")
        .not("#fieldCode")
        .val("")
        .trigger("change");

    $("#previewImage1, #previewImage2").hide();
  });
});

// Update Field
$("#updateBtn").on("click", function () {
  let formData = new FormData();
  formData.append("fieldName", $("#fieldName").val());
  formData.append("fieldLocation", $("#fieldLocation").val());
  formData.append("extentSize", $("#fieldSize").val());
  if ($("#fieldImage1")[0].files[0]) {
    formData.append("fieldImage1", $("#fieldImage1")[0].files[0]);
  }
  if ($("#fieldImage2")[0].files[0]) {
    formData.append("fieldImage2", $("#fieldImage2")[0].files[0]);
  }

  $.ajax({
    url: `http://localhost:5050/cropmonitoring/api/v1/fields/${$(
        "#fieldCode"
    ).val()}`,
    type: "PATCH",
    data: formData,
    contentType: false,
    processData: false,
    headers: {
      Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
    },
    success: function () {
      alert("Field updated successfully!");
    },

    error: function (xhr) {
      if (xhr.status === 401) {
        // Handle session expiration
        if (confirm("Session expired. Please log in again.")) {
          window.location.href = "/index.html";
        }
      } else if (xhr.status === 403) {
        // Handle insufficient permissions
        alert("You do not have permission to perform this action.");
      } else {
        // Handle other errors
        alert("Error updating field: " + (xhr.responseText || "An unexpected error occurred."));
      }
    },
  });
});

// Delete Field
$("#deleteBtn").on("click", function () {
  const fieldCode = $("#fieldCode").val();
  if (confirm("Are you sure you want to delete this field?")) {
    $.ajax({
      url: `http://localhost:5050/cropmonitoring/api/v1/fields/${fieldCode}`,
      type: "DELETE",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function () {
        alert("Field deleted successfully!");
        $("#fieldForm")[0].reset();
        generateFieldCode();
      },

      error: function (xhr) {
        if (xhr.status === 401) {
          // Handle session expiration
          if (confirm("Session expired. Please log in again.")) {
            window.location.href = "/index.html";
          }
        } else if (xhr.status === 403) {
          // Handle insufficient permissions
          alert("You do not have permission to perform this action.");
        } else {
          // Handle other errors
          alert("Error deleting field: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  }
});

// Search Field
$(document).ready(function () {
  $("#searchField").on("keypress", function (e) {
    if (e.key === "Enter") {
      e.preventDefault();
      performSearch();
    }
  });

  $("#searchIcon").on("click", function () {
    performSearch();
  });

  function performSearch() {
    const searchTerm = $("#searchField").val().trim();
    if (!searchTerm) {
      alert("Please enter a field code or name to search.");
      return;
    }

    $.ajax({
      url: `http://localhost:5050/cropmonitoring/api/v1/fields?searchTerm=${encodeURIComponent(
          searchTerm
      )}`,
      type: "GET",
      contentType: "application/json",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (data) {
        if (data.length > 0) {
          const field = data[0]; // Use the first matched field
          $("#fieldCode").val(field.fieldCode);
          $("#fieldName").val(field.fieldName);
          $("#fieldLocation").val(field.fieldLocation);
          $("#fieldSize").val(field.extentSize);

          if (field.fieldImage1) {
            $("#previewImage1")
                .attr("src", `data:image/png;base64,${field.fieldImage1}`)
                .show();
          } else {
            $("#previewImage1").hide();
          }

          if (field.fieldImage2) {
            $("#previewImage2")
                .attr("src", `data:image/png;base64,${field.fieldImage2}`)
                .show();
          } else {
            $("#previewImage2").hide();
          }
        } else {
          alert(
              "Field not found. Please check the field code or name and try again."
          );
        }
      },

      error: function (xhr) {
        if (xhr.status === 401)
            // Handle session expiration
          if (confirm("Session expired. Please log in again.")) {
            window.location.href = "/index.html";
          }
          else {
            // Handle other errors
            alert("Error searching field: " + (xhr.responseText || "An unexpected error occurred."));
          }
      },
    });
  }
});

$(document).ready(function () {
  $("#assigment").click(function () {
    window.location.href = "fieldStaffAssign.html";
  });
});


