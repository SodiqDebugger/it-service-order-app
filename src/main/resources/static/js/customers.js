document.addEventListener('DOMContentLoaded', function() {
    const customerList = document.getElementById('customerList');
    const addCustomerButton = document.getElementById('addCustomerButton');
    const customerModal = document.getElementById('customerModal');
    const closeButton = customerModal.querySelector('.close-button');
    const customerForm = document.getElementById('customerForm');
    const customerIdInput = document.getElementById('customerId');
    const customerNameInput = document.getElementById('customerName');
    const customerEmailInput = document.getElementById('customerEmail');
    const customerPhoneInput = document.getElementById('customerPhone');
    const cancelCustomerButton = document.getElementById('cancelCustomerButton');

    function fetchCustomers() {
        fetch('/api/customers')
            .then(response => response.json())
            .then(customers => {
                customerList.innerHTML = '';
                customers.forEach(customer => {
                    const li = document.createElement('li');
                    li.innerHTML = `
                        <span>${customer.name} - <span class="math-inline">\{customer\.email\} \(</span>{customer.phone || 'Telefon raqami yo\'q'})</span>
                        <div>
                            <button class="btn-primary edit-btn" data-id="<span class="math-inline">\{customer\.id\}"\>Tahrirlash</button\>
                            <button class="btn-primary delete-btn" data-id="{customer.id}">O'chirish</button>
</div>
`;
                    customerList.appendChild(li);
                });
                addEventListenersToButtons();
            })
            .catch(error => console.error('Mijozlarni yuklashda xato:', error));
    }
    function addEventListenersToButtons() {
        document.querySelectorAll('.edit-btn').forEach(button => {
            button.onclick = function() {
                const customerId = this.dataset.id;
                fetch(`/api/customers/${customerId}`)
                    .then(response => response.json())
                    .then(customer => {
                        customerIdInput.value = customer.id;
                        customerNameInput.value = customer.name;
                        customerEmailInput.value = customer.email;
                        customerPhoneInput.value = customer.phone;
                        customerModal.style.display = 'flex';
                    })
                    .catch(error => console.error('Mijozni tahrirlash uchun yuklashda xato:', error));
            };
        });

        document.querySelectorAll('.delete-btn').forEach(button => {
            button.onclick = function() {
                const customerId = this.dataset.id;
                if (confirm('Rostdan ham bu mijozni o\'chirmoqchimisiz?')) {
                    fetch(`/api/customers/${customerId}`, {
                        method: 'DELETE'
                    })
                        .then(response => {
                            if (response.ok) {
                                alert('Mijoz muvaffaqiyatli o\'chirildi.');
                                fetchCustomers();
                            } else {
                                alert('Mijozni o\'chirishda xato yuz berdi.');
                            }
                        })
                        .catch(error => console.error('O\'chirishda xato:', error));
                }
            };
        });
    }

    addCustomerButton.addEventListener('click', function() {
        customerIdInput.value = '';
        customerForm.reset();
        customerModal.style.display = 'flex';
    });

    closeButton.addEventListener('click', function() {
        customerModal.style.display = 'none';
    });

    cancelCustomerButton.addEventListener('click', function() {
        customerModal.style.display = 'none';
    });

    window.addEventListener('click', function(event) {
        if (event.target === customerModal) {
            customerModal.style.display = 'none';
        }
    });

    customerForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const customerId = customerIdInput.value;
        const customerData = {
            name: customerNameInput.value,
            email: customerEmailInput.value,
            phone: customerPhoneInput.value
        };

        let url = '/api/customers';
        let method = 'POST';

        if (customerId) {
            url = `/api/customers/${customerId}`;
            method = 'PUT';
        }

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customerData)
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Server xatosi: ' + response.statusText);
            })
            .then(data => {
                alert('Mijoz muvaffaqiyatli saqlandi!');
                customerModal.style.display = 'none';
                fetchCustomers();
            })
            .catch(error => {
                console.error('Mijozni saqlashda xato:', error);
                alert('Mijozni saqlashda xato yuz berdi: ' + error.message);
            });
    });

    fetchCustomers();
});
