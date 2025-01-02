document.addEventListener("DOMContentLoaded", () => {
    const inputToggleButton = document.querySelector(".input-button"); // 입력 버튼 선택
    const deliveryRequestSection = document.querySelector("label.test"); // 숨겨진 입력창 선택

    inputToggleButton.addEventListener("click", () => {
        // visible 클래스 추가/제거
        if (deliveryRequestSection.classList.contains("visible")) {
            deliveryRequestSection.classList.remove("visible"); // 숨김
            inputToggleButton.classList.remove("clicked"); // 버튼 크기 복구
            inputToggleButton.textContent = "입력하기"; // 버튼 텍스트 복구
        } else {
            deliveryRequestSection.classList.add("visible"); // 표시
            inputToggleButton.classList.add("clicked"); // 버튼 크기 확장
            inputToggleButton.textContent = ""; // 버튼 텍스트 제거
        }
    });
});



document.addEventListener('DOMContentLoaded', () => {
    const updateTotalPrice =() => {
        const items = document.querySelectorAll('.item');
        let totalPrice = 0;

        items.forEach(item => {
           const itemPrice = parseInt(item.querySelector('.item-price >  ._text')?.textContent.replace(/[^0-9]/g, ''), 10);
            const itemQuantity = parseInt(item.querySelector('.quantity')?.textContent.replace(/[^0-9]/g, ''), 10);
            if (!isNaN(itemPrice) && !isNaN(itemQuantity)){
                totalPrice += itemPrice;
            }
        });

        const totalPriceElements = document.querySelectorAll('.pay-total-price, .total, .pay-price, .item-total-price');
        totalPriceElements.forEach(element => {
            element.textContent = `${totalPrice.toLocaleString()}`
        });

        const payButton = document.querySelector('.pay-button > .pay-price');
        if (payButton){
            payButton.textContent = `${totalPrice.toLocaleString()}원 결제하기`
        }
    };

    updateTotalPrice();


    const $payButton = document.querySelector('.pay-button');

    $payButton.addEventListener('click', () => {
        const items = document.querySelectorAll('.item');
        if (!items.length){
            alert("결제할 상품이 없습니다.");
            return;
        }

        const formData = new FormData();
        items.forEach((item, index) => {
            const idElement = item.querySelector('.id')?.value;
            const itemNameElement = item.querySelector('.item-name-container > ._text')?.textContent;
            const itemPriceElement = item.querySelector('.item-price > ._text')?.textContent.replace(/[^0-9]/g, '');
            const itemQuantityElement = item.querySelector('.quantity')?.textContent.replace(/[^0-9]/g, '');
            const itemImageElement = item.querySelector('.image-container img')?.src;
            // `?`의 의미 `querySelector`로 요소를 찾지 못했을 경우 `null`을 반환하기 때문에 해당 접근 시 오류가 발생할 수 있음. 이를 방지 하기 위해 요소 선택 후 `null` 체크를 하기 위한 로직
            if (!idElement || !itemNameElement || !itemPriceElement || !itemQuantityElement || !itemImageElement) {
                console.error(`Item ${index} is missing required fields.`);
                return;
            }


            formData.append(`items[${index}].payItemId`, idElement);
            formData.append(`items[${index}].payItemName`, itemNameElement);
            formData.append(`items[${index}].payItemPrice`, itemPriceElement);
            formData.append(`items[${index}].payQuantity`, itemQuantityElement);
            formData.append(`items[${index}].itemImage`, itemImageElement);
        });
        const totalPrice = document.querySelector('.pay-total-price').textContent.replace(/[^0-9]/g, '');
        formData.append('totalPrice', totalPrice);

        const xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
              return;
              }
              if (xhr.status < 200 || xhr.status >= 300) {
                  alert('결제 처리 중 문제가 발생했습니다.')
              return;
              }
            // 서버 응답에 따라 리다이렉트 또는 추가 동작
            const response = JSON.parse(xhr.responseText);
            if (response.status === 'success') {
                alert(response.message);
                window.location.href = './record';
            } else {
                alert(response.message);
            }

        };
        xhr.open('POST', '/pay/submit', true);
        xhr.send(formData);
    });
});