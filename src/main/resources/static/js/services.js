document.addEventListener('DOMContentLoaded', function () {
    const serviceList = document.getElementById('serviceList');
    const addServiceButton = document.getElementById('addServiceButton');
    const serviceModal = document.getElementById('serviceModal');
    const closeButton = serviceModal.querySelector('.close-button');
    const serviceForm = document.getElementById('serviceForm');
    const serviceIdInput = document.getElementById('serviceId');
    const serviceNameInput = document.getElementById('serviceName');
    const serviceDescriptionInput = document.getElementById('serviceDescription');
    const servicePriceInput = document.getElementById('servicePrice');
    const cancelServiceButton = document.getElementById('cancelServiceButton');

    function fetchServices() {
        fetch('/api/services')
            .then(response => response.json())
            .then(services => {
                serviceList.innerHTML = '';
                services.forEach(service => {
                    const li = document.createElement('li');
                    li.innerHTML = `
                        <span>${service.name} - $<span class="math-inline">\{service\.price\.toFixed\(2\)\}</span\>
                        <p>{service.description}</p>
<div>
<button class="btn-primary edit-btn" data-id="service.id">Tahrirlash</button><buttonclass="btn−primarydelete−btn"data−id="{service.id}">O'chirish</button>
</div>
`;
                    serviceList.appendChild(li);
                });
                addEventListenersToButtons();
            })
            .catch(error => console.error('Xizmatlarni yuklashda xato:', error));
    }

    function addEventListenersToButtons() {
        document.querySelectorAll('.edit-btn').forEach(button => {
            button.onclick = function () {
                const serviceId = this.dataset.id;
                fetch(`/api/services/${serviceId}`)
                    .then(response => response.json())
                    .then(service => {
                        serviceIdInput.value = service.id;
                        serviceNameInput.value = service.name;
                        serviceDescriptionInput.value = service.description;
                        servicePriceInput.value = service.price;
                        serviceModal.style.display = 'flex'; // Modalni ko'rsatish
                    })
                    .catch(error => console.error('Xizmatni tahrirlash uchun yuklashda xato:', error));
            };
        });

        document.querySelectorAll('.delete-btn').forEach(button => {
            button.onclick = function () {
                const serviceId = this.dataset.id;
                if (confirm('Rostdan ham bu xizmatni o\'chirmoqchimisiz?')) {
                    fetch(`/api/services/${serviceId}`, {
                        method: 'DELETE'
                    })
                        .then(response => {
                            if (response.ok) {
                                alert('Xizmat muvaffaqiyatli o\'chirildi.');
                                fetchServices();
                            } else {
                                alert('Xizmatni o\'chirishda xato yuz berdi.');
                            }
                        })
                        .catch(error => console.error('O\'chirishda xato:', error));
                }
            };
        });
    }

    addServiceButton.addEventListener('click', function () {
        serviceIdInput.value = '';
        serviceForm.reset();
        serviceModal.style.display = 'flex';
    });

    closeButton.addEventListener('click', function () {
        serviceModal.style.display = 'none';
    });

    cancelServiceButton.addEventListener('click', function () {
        serviceModal.style.display = 'none';
    });

    window.addEventListener('click', function (event) {
        if (event.target === serviceModal) {
            serviceModal.style.display = 'none';
        }
    });

    serviceForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const serviceId = serviceIdInput.value;
        const serviceData = {
            name: serviceNameInput.value,
            description: serviceDescriptionInput.value,
            price: parseFloat(servicePriceInput.value)
        };

        let url = '/api/services';
        let method = 'POST';

        if (serviceId) {
            url = `/api/services/${serviceId}`;
            method = 'PUT';
        }

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(serviceData)
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Server xatosi: ' + response.statusText);
            })
            .then(data => {
                alert('Xizmat muvaffaqiyatli saqlandi!');
                serviceModal.style.display = 'none';
                fetchServices();
            })
            .catch(error => {
                console.error('Xizmatni saqlashda xato:', error);
                alert('Xizmatni saqlashda xato yuz berdi: ' + error.message);
            });
    });

    fetchServices();
});
