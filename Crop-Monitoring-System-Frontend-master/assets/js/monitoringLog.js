// Generate Monitoring Log Code
function generateLogCode() {
  const code = "L-" + Math.floor(1000 + Math.random() * 900);
  $("#logCode").val(code);
}

$(document).ready(function () {
  generateLogCode();
  loadFields();
  loadCrops();
  loadStaff();

  // Load fields into dropdown
  function loadFields() {
    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/fields/allFields",
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (fields) {
        $("#fieldSelect")
          .empty()
          .append("<option disabled selected>Select Field</option>");
        fields.forEach((field) => {
          $("#fieldSelect").append(
            new Option(
              `${field.fieldCode} - ${field.fieldName}`,
              field.fieldCode
            )
          );
        });
      },
      error: function () {
        alert("Failed to load fields.");
      },
    });
  }

  // Load crops into dropdown
  function loadCrops() {
    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/crops/allcrops",
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (crops) {
        $("#cropSelect")
          .empty()
          .append("<option disabled selected>Select Crop</option>");
        crops.forEach((crop) => {
          $("#cropSelect").append(
            new Option(
              `${crop.cropCode} - ${crop.cropCommonName}`,
              crop.cropCode
            )
          );
        });
      },
      error: function () {
        alert("Failed to load crops.");
      },
    });
  }

  // Load staff into dropdown
  function loadStaff() {
    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/staff/allstaff",
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (staffList) {
        $("#staffSelect")
          .empty()
          .append("<option disabled selected>Select Staff</option>");
        staffList.forEach((staff) => {
          $("#staffSelect").append(
            new Option(`${staff.id} - ${staff.firstName}`, staff.id)
          );
        });
      },
      error: function () {
        alert("Failed to load staff.");
      },
    });
  }

  // Save Monitoring Log
  $("#monitoringLogForm").on("submit", function (e) {
    e.preventDefault();
    let formData = new FormData(this);
    formData.append("logCode", $("#logCode").val());
    formData.append("logDate", $("#logDate").val());
    formData.append("observation", $("#logDetails").val());
    formData.append("logImage", $("#observedImage")[0].files[0]);
    formData.append("fieldCode", $("#fieldSelect").val());
    formData.append("cropCode", $("#cropSelect").val());
    formData.append("staffId", $("#staffSelect").val());

    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/monitoringLog",
      type: "POST",
      data: formData,
      contentType: false,
      processData: false,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function () {
        alert("Monitoring log saved successfully!");
        $("#monitoringLogForm")[0].reset();
        generateLogCode();
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
          alert("Error saving monitoring log: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  });

  // Get All Monitoring Logs
  $("#getAllLogsBtn").on("click", function () {
    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/monitoringLog/allLogs",
      type: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (logs) {
        let tableBody = $("#logTableBody");
        tableBody.empty();
        logs.forEach((log) => {
          tableBody.append(`
              <tr>
                <td>${log.log_code}</td>
                <td>${log.log_date}</td>
                <td>${log.observation}</td>
                <td>${log.fieldCode}</td>
                <td>${log.cropCode}</td>
                <td>${log.id}</td>
                <td><img src="data:image/png;base64,${log.log_image}" style="max-height: 50px;"></td>
              </tr>
            `);
        });
        $("#logListModal").modal("show");
      },

      error: function (xhr) {
        if (xhr.status === 401) 
          // Handle session expiration
          if (confirm("Session expired. Please log in again.")) {
            window.location.href = "/index.html";
          }
        else {
          // Handle other errors
          alert("Error retrieving monitoring logs : " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  });

  // Search Monitoring Log
  $("#searchIcon").on("click", searchAndFillLogForm);
  $("#searchLog").on("keypress", function (e) {
    if (e.which === 13) searchAndFillLogForm();
  });

  function searchAndFillLogForm() {
    const searchTerm = $("#searchLog").val().trim();
    if (searchTerm === "") {
      alert("Enter log code or observation.");
      return;
    }

    $.ajax({
      url: `http://localhost:5050/cropmonitoring/api/v1/monitoringLog?searchTerm=${encodeURIComponent(
        searchTerm
      )}`,
      type: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (logs) {
        if (logs.length === 0) {
          alert("No matching log found.");
          return;
        }

        const log = logs[0];
        $("#logCode").val(log.log_code);
        $("#logDate").val(log.log_date);
        $("#logDetails").val(log.observation);
        $("#fieldSelect").val(log.fieldCode).change();
        $("#cropSelect").val(log.cropCode).change();
        $("#staffSelect").val(log.id).change();

        if (log.log_image) {
          $("#previewObservedImage")
            .attr("src", `data:image/png;base64,${log.log_image}`)
            .show();
        } else {
          $("#previewObservedImage").hide();
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
          alert("Error retrieving monitoring log : " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  }

  // Update Monitoring Log
  $("#updateLogBtn").on("click", function () {
    let formData = new FormData();
    formData.append("logDate", $("#logDate").val());
    formData.append("observation", $("#logDetails").val());
    formData.append("fieldCode", $("#fieldSelect").val());
    formData.append("cropCode", $("#cropSelect").val());
    formData.append("staffId", $("#staffSelect").val());
    if ($("#observedImage")[0].files[0]) {
      formData.append("logImage", $("#observedImage")[0].files[0]);
    }

    $.ajax({
      url: `http://localhost:5050/cropmonitoring/api/v1/monitoringLog/${$(
        "#logCode"
      ).val()}`,
      type: "PATCH",
      data: formData,
      contentType: false,
      processData: false,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function () {
        alert("Monitoring log updated successfully!");
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
          alert("Error updating monitoring log : " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  });

  // Delete Monitoring Log
  $("#deleteLogBtn").on("click", function () {
    const logCode = $("#logCode").val();
    if (confirm("Are you sure you want to delete this log?")) {
      $.ajax({
        url: `http://localhost:5050/cropmonitoring/api/v1/monitoringLog/${logCode}`,
        type: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
        success: function () {
          alert("Monitoring log deleted successfully!");
          $("#logForm")[0].reset();
          generateLogCode();
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
            alert("Error delete monitoring log: " + (xhr.responseText || "An unexpected error occurred."));
          }
        },
      });
    }
  });

  // Clear Monitoring Log Form
  $("#clearLogBtn").on("click", function () {
    $("#monitoringLogForm")[0].reset();
    generateLogCode();
    $("#previewObservedImage").hide();
  });
});
