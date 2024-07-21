document.getElementById('phoneForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const phoneNumber = document.getElementById('phoneNumber').value;
    const resultElement = document.getElementById('countryName');

    // Clear previous result
    resultElement.textContent = '';

    // Validate phone number format
    if (!phoneNumber) {
        resultElement.textContent = 'Please enter a phone number.';
        return;
    }

    const phoneNumberPattern = /^\d{10,15}$/;
    if (!phoneNumberPattern.test(phoneNumber)) {
        resultElement.textContent = 'Please enter a valid phone number with 10 to 15 digits.';
        return;
    }

    // Log the phone number being sent
    console.log('Sending phone number to backend:', phoneNumber);

    // Send request to backend
    fetch(`http://localhost:8088/api/country?phoneNumber=${encodeURIComponent(phoneNumber)}`, {
        method: 'POST', // Убедитесь, что метод совпадает с тем, который используется на сервере
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        // Log the raw response
        console.log('Raw response:', response);

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Log the parsed JSON data
        console.log('Parsed response data:', data);

        if (data.country) {
            resultElement.textContent = `Country: ${data.country}`;
        } else {
            resultElement.textContent = 'Country not found.';
        }
    })
    .catch(error => {
        // Log the error
        console.error('Error:', error);

        resultElement.textContent = 'Error: ' + error.message;
    });
});

document.getElementById('phoneNumber').addEventListener('input', function(event) {
    const inputElement = event.target;
    const resultElement = document.getElementById('countryName');
    const phoneNumberPattern = /^[0-9]*$/;

    // Clear previous result
    resultElement.textContent = '';

    // Check if input contains only digits
    if (!phoneNumberPattern.test(inputElement.value)) {
        resultElement.textContent = 'Please enter a valid phone number (digits only).';
        inputElement.value = inputElement.value.replace(/[^0-9]/g, '');
    }
});
