const origin = window.location.origin;
console.log(origin);
const api = origin + "/school/students";

async function apiCall(url, options = {}) {
  const token = localStorage.getItem("token");

  options.headers = {
    ...(options.headers || {}),
    "Authorization": "Bearer " + token,
    "Content-Type": "application/json"
  };

  return fetch(url, options);
}

async function loadStudents() {
  const token = localStorage.getItem("token");
  if (!token) {
    alert("No token found. Redirecting to login.");
    window.location.href = "/login.html";
    return;
  }
  const res = await apiCall(api);
    if (res.status === 401 || res.status === 403) {
       alert("Session expired. Please log in again.");
       localStorage.removeItem("token");
       window.location.href = "/login.html";
       return;
     }
     if (res.status == 429) {
        window.location.href = "/429.html";
        return;
     }
  const data = await res.json();
  const tbody = document.querySelector("#studentsTable tbody");
  tbody.innerHTML = "";
  console.log("status is " + res.status);
  if (res.status == 429) {
   const tr = document.createElement("tr");
    tr.innerHTML = `
       <td>Too many requests. Please try again later</td>
    `;
    tbody.appendChild(tr);
    return;
  }
  data.forEach(s => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${s.id}</td>
      <td>${s.name}</td>
      <td>${s.email}</td>
      <td>${s.age}</td>
      <td>
        <button onclick="editStudent(${s.id})">Edit</button>
        <button onclick="deleteStudent(${s.id})">Delete</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

async function saveStudent(e) {
  e.preventDefault();
  const id = document.getElementById("studentId").value;
  const student = {
    name: document.getElementById("name").value,
    email: document.getElementById("email").value,
    age: parseInt(document.getElementById("age").value)
  };

  const method = id ? "PUT" : "POST";
  const url = id ? `${api}/${id}` : api;
  await apiCall(url, {
    method,
//    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(student)
  });
  document.getElementById("studentId").value = "";

  e.target.reset();
  loadStudents();
}

async function editStudent(id) {
  const res = await apiCall(`${api}/${id}`);
  const s = await res.json();
  document.getElementById("studentId").value = s.id;
  document.getElementById("name").value = s.name;
  document.getElementById("email").value = s.email;
  document.getElementById("age").value = s.age;
}

async function deleteStudent(id) {
  await apiCall(`${api}/${id}`, { method: "DELETE" });
  loadStudents();
}

document.getElementById("studentForm").addEventListener("submit", saveStudent);
loadStudents();
