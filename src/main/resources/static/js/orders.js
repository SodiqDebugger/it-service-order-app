document.addEventListener('DOMContentLoaded', function() {
    const orderList = document.getElementById('orderList');
    const createOrderButton = document.getElementById('createOrderButton');
    const orderModal = document.getElementById('orderModal');
    const closeButton = orderModal.querySelector('.close-button');
    const orderForm = document.getElementById('orderForm');
    const orderCustomerIdSelect = document.getElementById('orderCustomerId');
    const servicesSelectionDiv = document.getElementById('servicesSelection');
    const cancelOrderButton = document.getElementById('cancelOrderButton');

    function fetchOrders() {
        fetch('/api/orders')
            .then(response => response.json())
            .then(orders => {
                orderList.innerHTML = '';
                orders.forEach(order => {
                    const li = document.createElement('li');
                    li.innerHTML = `
                        <span>Buyurtma ID: ${order.id} | Mijoz: ${order.customer ? order.customer.name : 'Noma\'lum mijoz'}</span>
                        <p>Sana: ${new Date(order.orderDate).toLocaleString()} | Jami: $${order.totalAmount.toFixed(2)} | Status: <span class="math-inline">\{order\.status\}</p\>
                        <div>
<button class="btn-primary view-details-btn" data-id="{order.id}">Tafsilotlar</button>
<button class="btn-primary delete-btn" data-id="${order.id}">O'chirish</button>
</div>
`;
                    orderList.appendChild(li);
                });
                addEventListenersToButtons();
            })
            .catch(error => console.error('Buyurtmalarni yuklashda xato:', error));
    }
    function addEventListenersToButtons() {
        document.querySelectorAll('.view-details-btn').forEach(button => {
            button.onclick = function() {
                const orderId = this.dataset.id;
                alert('Tafsilotlarni ko\'rish funksiyasi (ID: ' + orderId + '). Hozircha amalga oshirilmagan.');
            };
        });

        document.querySelectorAll('.delete-btn').forEach(button => {
            button.onclick = function() {
                const orderId = this.dataset.id;
                if (confirm('Rostdan ham bu buyurtmani o\'chirmoqchimisiz?')) {
                    fetch(`/api/orders/${orderId}`, {
                        method: 'DELETE'
                    })
                        .then(response => {
                            if (response.ok) {
                                alert('Buyurtma muvaffaqiyatli o\'chirildi.');
                                fetchOrders();
                            } else {
                                alert('Buyurtmani o\'chirishda xato yuz berdi.');
                            }
                        })
                        .catch(error => console.error('O\'chirishda xato:', error));
                }
            };
        });
    }

    // Yangi buyurtma yaratish modalini ochish
    createOrderButton.addEventListener('click', function() {
        orderForm.reset();
        populateCustomersDropdown(); // Mijozlar ro'yxatini yuklash
        populateServicesCheckboxes(); // Xizmatlar ro'yxatini yuklash
        orderModal.style.display = 'flex';
    });

    // Modalni yopish
    closeButton.addEventListener('click', function() {
        orderModal.style.display = 'none';
    });

    cancelOrderButton.addEventListener('click', function() {
        orderModal.style.display = 'none';
    });

    window.addEventListener('click', function(event) {
        if (event.target === orderModal) {
            orderModal.style.display = 'none';
        }
    });

    // Mijozlar dropdownini to'ldirish
    function populateCustomersDropdown() {
        fetch('/api/customers')
            .then(response => response.json())
            .then(customers => {
                orderCustomerIdSelect.innerHTML = '<option value="">Mijozni tanlang</option>';
                customers.forEach(customer => {
                    const option = document.createElement('option');
                    option.value = customer.id;
                    option.textContent = customer.name;
                    orderCustomerIdSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Mijozlarni yuklashda xato:', error));
    }

    // Xizmatlar checkboxlarini to'ldirish
    function populateServicesCheckboxes() {
        fetch('/api/services')
            .then(response => response.json())
            .then(services => {
                servicesSelectionDiv.innerHTML = '';
                services.forEach(service => {
                    const serviceDiv = document.createElement('div');
                    serviceDiv.className = 'form-group service-item';
                    serviceDiv.innerHTML = `
                        <input type="checkbox" id="service-<span class="math-inline">\{service\.id\}" value\="</span>{service.id}" data-price="<span class="math-inline">\{service\.price\}"\>
                        <label for="service-{service.id}">service.name(service.price.toFixed(2))</label><inputtype="number"class="service−quantity"data−service−id="{service.id}" value="1" min="1" style="width: 60px; margin-left: 10px; display: none;">
`;
                    servicesSelectionDiv.appendChild(serviceDiv);
                    // Checkbox bosilganda quantity inputni ko'rsatish/yashirish
                    const checkbox = serviceDiv.querySelector(`input[type="checkbox"]`);
                    const quantityInput = serviceDiv.querySelector(`.service-quantity`);
                    checkbox.addEventListener('change', function() {
                        quantityInput.style.display = this.checked ? 'inline-block' : 'none';
                        if (!this.checked) {
                            quantityInput.value = 1; // Agar tanlanmasa, 1 ga qaytarish
                        }
                    });
                });
            })
            .catch(error => console.error('Xizmatlarni yuklashda xato:', error));
    }

    // Buyurtma formasini yuborish
    orderForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const customerId = orderCustomerIdSelect.value;
        if (!customerId) {
            alert('Iltimos, mijozni tanlang.');
            return;
        }

        const selectedServiceIds = [];
        const quantities = [];
        document.querySelectorAll('input[type="checkbox"]:checked').forEach(checkbox => {
            const serviceId = parseInt(checkbox.value);
            const quantity = parseInt(document.querySelector(`.service-quantity[data-service-id="${serviceId}"]`).value);
            selectedServiceIds.push(serviceId);
            quantities.push(quantity);
        });

        if (selectedServiceIds.length === 0) {
            alert('Iltimos, kamida bitta xizmatni tanlang.');
            return;
        }

        const orderData = {
            customerId: parseInt(customerId),
            serviceIds: selectedServiceIds,
            quantities: quantities
        };

        fetch('/api/orders/create', { // OrderController'dagi /api/orders/create endpointiga POST so'rov
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Buyurtma yaratishda server xatosi: ' + response.statusText);
            })
            .then(data => {
                alert('Buyurtma muvaffaqiyatli yaratildi!');
                orderModal.style.display = 'none';
                fetchOrders();
            })
            .catch(error => {
                console.error('Buyurtma yaratishda xato:', error);
                alert('Buyurtma yaratishda xato yuz berdi: ' + error.message);
            });
    });

    fetchOrders();
});
