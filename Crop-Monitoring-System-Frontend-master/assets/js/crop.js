// generate crop code
function generateCropCode() {
  const code = "C-" + Math.floor(1000 + Math.random() * 900);
  $("#cropCode").val(code);
}

$(document).ready(function () {
  generateCropCode();
  loadFields();

  // Load field to crop dropdown
  function loadFields() {
    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/fields/allFields", // Endpoint to fetch fields
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (fields) {
        $("#field")
          .empty()
          .append("<option disabled selected>Select Field</option>");

        fields.forEach((field) => {
          $("#field").append(
            new Option(
              `${field.fieldCode} - ${field.fieldName}`,
              field.fieldCode
            )
          );
        });
      },
      error: function (xhr) {
        console.error("Failed to load fields:", xhr.responseText);
      },
    });
  }

  // Save Crop
  $("#cropForm").on("submit", function (e) {
    e.preventDefault();

    let formData = new FormData(this);
    formData.append("cropCode", $("#cropCode").val());
    formData.append("cropCommonName", $("#cropCommonName").val());
    formData.append("cropScientificName", $("#cropScientificName").val());
    formData.append("category", $("#cropCategory").val());
    formData.append("cropSeason", $("#cropSeason").val());
    formData.append("fieldCode", $("#field").val());
    formData.append("cropImage", $("#cropImage")[0].files[0]);

    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/crops",
      type: "POST",
      data: formData,
      contentType: false,
      processData: false,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (response) {
        alert("Crop saved successfully!");
        $("#cropForm")[0].reset();
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
          alert("Error saving crop: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  });

  // Get All Crops
  $("#getAllBtn").on("click", function () {
    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/crops/allcrops",
      type: "GET",
      contentType: "application/json",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (data) {
        let tableBody = $("#cropTableBody");
        tableBody.empty();
        data.forEach((crop) => {
          tableBody.append(`
          <tr>
            <td>${crop.cropCode}</td>
            <td>${crop.cropCommonName}</td>
            <td>${crop.cropScientificName}</td>
            <td>${crop.category}</td>
            <td>${crop.cropSeason}</td>
            <td>${crop.fieldCode}</td>
            <td><img src="data:image/png;base64,${crop.cropImage}" alt="Crop Image" style="max-height: 50px;"></td>
          </tr>
        `);
        });
        $("#cropListModal").modal("show");
      },

      error: function (xhr) {
        if (xhr.status === 401) 
          // Handle session expiration
          if (confirm("Session expired. Please log in again.")) {
            window.location.href = "/index.html";
          }
        else {
          // Handle other errors
          alert("Error retrieving crop list: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  });

  // search crop
  $("#searchIcon").on("click", function () {
    searchAndFillCropForm();
  });

  $("#searchCrop").on("keypress", function (e) {
    if (e.which == 13) {
      searchAndFillCropForm();
    }
  });

  function searchAndFillCropForm() {
    const searchTerm = $("#searchCrop").val().trim();
    if (searchTerm === "") {
      alert("Please enter a crop code or common name.");
      return;
    }

    $.ajax({
      url: `http://localhost:5050/cropmonitoring/api/v1/crops?searchTerm=${encodeURIComponent(
        searchTerm
      )}`,
      type: "GET",
      contentType: "application/json",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (data) {
        if (data.length === 0) {
          alert("No matching crop found.");
          return;
        }

        const crop = data[0];
        $("#cropCode").val(crop.cropCode);
        $("#cropCommonName").val(crop.cropCommonName);
        $("#cropScientificName").val(crop.cropScientificName);
        $("#cropCategory").val(crop.category).change();
        $("#cropSeason").val(crop.cropSeason).change();
        $("#field").val(crop.fieldCode).change();

        if (crop.cropImage) {
          $("#previewCropImage")
            .attr("src", `data:image/png;base64,${crop.cropImage}`)
            .show();
        } else {
          $("#previewCropImage").hide();
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
          alert("Error searching crop: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  }

  // Update Crop
  $("#updateBtn").on("click", function () {
    let formData = new FormData();
    formData.append("cropCommonName", $("#cropCommonName").val());
    formData.append("cropScientificName", $("#cropScientificName").val());
    formData.append("category", $("#cropCategory").val());
    formData.append("cropSeason", $("#cropSeason").val());
    formData.append("fieldCode", $("#field").val());
    if ($("#cropImage")[0].files[0]) {
      formData.append("cropImage", $("#cropImage")[0].files[0]);
    }

    $.ajax({
      url: `http://localhost:5050/cropmonitoring/api/v1/crops/${$(
        "#cropCode"
      ).val()}`,
      type: "PATCH",
      data: formData,
      contentType: false,
      processData: false,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function () {
        alert("Crop updated successfully!");
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
          alert("Error updating crop: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  });

  // Delete Crop
  $("#deleteBtn").on("click", function () {
    const cropCode = $("#cropCode").val();
    if (confirm("Are you sure you want to delete this crop?")) {
      $.ajax({
        url: `http://localhost:5050/cropmonitoring/api/v1/crops/${cropCode}`,
        type: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
        success: function () {
          alert("Crop deleted successfully!");
          $("#cropForm")[0].reset();
          generateCropCode();
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
            alert("Error deleting crop: " + (xhr.responseText || "An unexpected error occurred."));
          }
        },
      });
    }
  });

  // Clear Crop Form
  $("#clearBtn").on("click", function () {
    $("#cropForm")[0].reset();
    generateCropCode();
    $("#previewCropImage").hide(); // hide preview if any
  });
});
