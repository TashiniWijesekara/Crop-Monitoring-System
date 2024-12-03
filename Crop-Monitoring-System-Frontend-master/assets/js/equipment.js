$(document).ready(function () {
  generateEquipmentID();
  loadFieldDropdown();
  loadStaffDropdown();

  // Generate Equipment ID
  function generateEquipmentID() {
    const id = "E-" + Math.floor(1000 + Math.random() * 9000);
    $("#equipmentId").val(id);
  }

  // Load Staff Dropdown
  function loadStaffDropdown() {
    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/staff/allstaff",
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (staffList) {
        staffList.forEach((staff) => {
          $("#assignedStaff").append(
            `<option value="${staff.id}">${staff.id} - ${staff.firstName} ${staff.lastName}</option>`
          );
        });
      },
      error: function () {
        alert("Failed to load staff data.");
      },
    });
  }

  // Load Field Dropdown
  function loadFieldDropdown() {
    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/fields/allFields",
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (fieldList) {
        fieldList.forEach((field) => {
          $("#assignedField").append(
            `<option value="${field.fieldCode}">${field.fieldCode} - ${field.fieldName}</option>`
          );
        });
      },
      error: function () {
        alert("Failed to load field data.");
      },
    });
  }

  // Save Equipment
  $("#equipmentForm").on("submit", function (e) {
    e.preventDefault();

    let formData = new FormData(this);
    formData.append("equipmentId", $("#equipmentId").val());
    formData.append("equipmentName", $("#equipmentName").val());
    formData.append("equipmentType", $("#equipmentType").val());
    formData.append("equipmentStatus", $("#status").val());
    formData.append("fieldCode", $("#assignedField").val());
    formData.append("id", $("#assignedStaff").val());

    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/equipment",
      type: "POST",
      data: JSON.stringify(Object.fromEntries(formData)),
      contentType: "application/json",
      processData: false,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (response) {
        alert("Equipment saved successfully!");
        $("#equipmentForm")[0].reset();
        //
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
          alert("Error saving equipment: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  });

  // Get All Equipment
  $("#getAllBtn").on("click", function () {
    $.ajax({
      url: "http://localhost:5050/cropmonitoring/api/v1/equipment/allEquipment",
      type: "GET",
      contentType: "application/json",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (data) {
        let tableBody = $("#equipmentTableBody");
        tableBody.empty();
        data.forEach((equipment) => {
          tableBody.append(`
          <tr>
            <td>${equipment.equipmentId}</td>
            <td>${equipment.equipmentName}</td>
            <td>${equipment.equipmentType}</td>
            <td>${equipment.equipmentStatus}</td>
            <td>${equipment.id}</td>
            <td>${equipment.fieldCode}</td>
          </tr>
        `);
        });
        $("#equipmentListModal").modal("show");
      },

      error: function (xhr) {
        if (xhr.status === 401) 
          // Handle session expiration
          if (confirm("Session expired. Please log in again.")) {
            window.location.href = "/index.html";
          }
        else {
          // Handle other errors
          alert("Error retrieving equipment list: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  });

  // Search Equipment
  $("#searchIcon").on("click", function () {
    searchAndFillEquipmentForm();
  });

  $("#searchEquipment").on("keypress", function (e) {
    if (e.which == 13) {
      searchAndFillEquipmentForm();
    }
  });

  function searchAndFillEquipmentForm() {
    const searchTerm = $("#searchEquipment").val().trim();
    if (searchTerm === "") {
      alert("Please enter an equipment code or name.");
      return;
    }

    $.ajax({
      url: `http://localhost:5050/cropmonitoring/api/v1/equipment?searchTerm=${encodeURIComponent(
        searchTerm
      )}`,
      type: "GET",
      contentType: "application/json",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function (data) {
        if (data.length === 0) {
          alert("No matching equipment found.");
          return;
        }

        const equipment = data[0];
        $("#equipmentId").val(equipment.equipmentId);
        $("#equipmentName").val(equipment.equipmentName);
        $("#equipmentType").val(equipment.equipmentType).change();
        $("#status").val(equipment.equipmentStatus).change();
        $("#assignedField").val(equipment.fieldCode).change();
        $("#assignedStaff").val(equipment.id).change();
      },

      error: function (xhr) {
        if (xhr.status === 401) 
          // Handle session expiration
          if (confirm("Session expired. Please log in again.")) {
            window.location.href = "/index.html";
          }
        else {
          // Handle other errors
          alert("Error searching equipment: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  }

  // Update Equipment
  $("#updateBtn").click(function () {
    const equipmentId = $("#equipmentId").val();
    if (!equipmentId) return alert("Please enter Equipment Id to update.");

    let formData = {
      equipmentName: $("#equipmentName").val(),
      equipmentType: $("#equipmentType").val(),
      equipmentStatus: $("#status").val(),
      fieldCode: $("#assignedField").val(),
      id: $("#assignedStaff").val(),
    };

    $.ajax({
      url: `http://localhost:5050/cropmonitoring/api/v1/equipment/${equipmentId}`,
      type: "PATCH",
      data: JSON.stringify(formData),
      contentType: "application/json",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function () {
        alert("Equipment updated successfully!");
        clearForm();
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
          alert("Error update Equipment: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  });

  // Delete Equipment
  $("#deleteBtn").click(function () {
    const equipmentId = $("#equipmentId").val();
    if (!equipmentId) return alert("Please enter Equipment Id to delete.");

    $.ajax({
      url: `http://localhost:5050/cropmonitoring/api/v1/equipment/${equipmentId}`,
      type: "DELETE",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
      success: function () {
        alert("Equipment deleted successfully!");
        clearForm();
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
          alert("Error delete Equipment: " + (xhr.responseText || "An unexpected error occurred."));
        }
      },
    });
  });

  // Clear form
  $(document).ready(function () {
    generateEquipmentID();

    $("#clearBtn").on("click", function () {
      generateEquipmentID();

      $("#equipmentForm")
        .find("input, select, textarea")
        .not("#equipmentId")
        .val("")
        .trigger("change");
    });
  });
});
