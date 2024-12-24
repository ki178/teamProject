const $main = document.getElementById('main');
const $list = document.getElementById('list');
const $itemContainer = document.getElementById('item-container');
const $pay = document.getElementById('pay');


// 이벤트 리스너 추가
document.querySelectorAll('.plus').forEach(button => {
    button.addEventListener('click', event => updateQuantity(event, 'plus'));
});
document.querySelectorAll('.minus').forEach(button => {
    button.addEventListener('click', event => updateQuantity(event, 'minus'));
});
document.querySelectorAll('.checkbox').forEach(checkbox => {
    checkbox.addEventListener('change', calculateTotal);
});

{
    function selectAll(selectAll) {
        const $checkboxes = document.getElementsByName('check');
        $checkboxes.forEach((checkbox) => {
            checkbox.checked = selectAll.checked;
            sendCheckboxStatus(checkbox);
        });
        calculateTotal();
    }

// 개별 checkbox 클릭 이벤트 추가
    document.addEventListener('DOMContentLoaded', () => {
        // 체크박스 초기 상태에 따라 총합 계산
        document.querySelectorAll('.checkbox').forEach(checkbox => {
            const currentItem = checkbox.closest('.item');
            if (!currentItem) {
                return;
            }
            const itemIdElement = currentItem.querySelector('.id');
            const itemPriceElement = currentItem.querySelector('.itemPrice');
            if (!itemIdElement || !itemPriceElement) {
                return;
            }

            // 초기 체크 상태 기반으로 동작
            if (checkbox.checked) {
                const itemPriceElement = currentItem.querySelector('.itemPrice');
                const itemPrice = parseInt(itemPriceElement.innerText.replace(/[^0-9]/g, ''), 10);


                // 총합 계산 초기화
                if (!isNaN(itemPrice)) {
                    checkbox.dataset.price = itemPrice;
                }
            }

            checkbox.addEventListener('change', () => {
                sendCheckboxStatus(checkbox); // 클릭한 checkbox의 상태를 서버로 전송
                calculateTotal(); // 상태 변경 후 총합 재계산
            });
        });
        calculateTotal(); // 초기 총합 계산
    });

// 개별 checkbox 상태 변경 시 호출
    function sendCheckboxStatus(checkbox) {
        const currentItem = checkbox.closest('.item');
        if (!currentItem) {
            return;
        }
        const itemIdElement = currentItem.querySelector('.id');
        if (!itemIdElement) {
            return;
        }
        const itemId = itemIdElement.value.trim();
        const isChecked = checkbox.checked ? 1 : 0;

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('itemId', itemId);
        formData.append('isChecked', isChecked);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {

                return;
            }
        };
        xhr.open('POST', '/cart/updateCheck');
        xhr.send(formData);

    }

// 총합 계산
    function calculateTotal() {
        const formData = new FormData();
        let totalItemPrice = 0;
        document.querySelectorAll('.item').forEach(item => {
            const checkbox = item.querySelector('.checkbox');
            if (!checkbox) {
                console.warn("Warning: Checkbox not found in item", item);
                return;
            }
            if (checkbox && checkbox.checked) {
                const $itemIdElement = item.querySelector('.id');
                const $itemPriceElement = item.querySelector('.itemPrice');
                if (!$itemIdElement || !$itemPriceElement) {
                    return;
                }
                const $itemId = $itemIdElement.value.trim();
                const $itemPrice = parseInt($itemPriceElement.innerText.replace(/[^0-9]/g, ''), 10);
                if (!$itemId || isNaN($itemPrice)) {

                    return;
                }
                formData.append('itemId', $itemId);
                formData.append('itemPrice', $itemPrice.toString());
                totalItemPrice += $itemPrice;

            }
        });


        const xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {

                return;
            }

            const response = JSON.parse(xhr.responseText);
            const totalPrice = response['totalPrice'] || 0;
            updateUI(totalPrice);

            function updateUI(totalPrice) {
                const priceText = `${totalPrice.toLocaleString()} 원`;
                const buttonText = totalPrice > 0 ? `${priceText} 주문하기` : "상품을 선택해주세요";

                document.querySelector('.pay > .price').innerText = priceText;
                document.querySelector('.total-pay > .price').innerText = priceText;
                document.querySelector('.pay-button').innerText = buttonText;

                if (totalPrice === 0) {
                    document.querySelector('.items-price > ._text').innerText = `상품 0 원 + 배송비 무료`;
                    document.querySelector('.total-price').innerText = "0 원";
                } else {
                    document.querySelector('.items-price > ._text').innerText = `상품 ${totalPrice.toLocaleString()} 원 + 배송비 무료`;
                    document.querySelector('.total-price').innerText = priceText;
                }
            }


        };
        xhr.open('POST', '/cart/totalPrice');
        xhr.send(formData);
    }

// 초기화 및 이벤트 등록
    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('.checkbox').forEach(checkbox => {
            checkbox.addEventListener('change', () => {
                sendCheckboxStatus(checkbox);
                calculateTotal();
            });
        });


        calculateTotal(); // 페이지 로드 시 총합 계산
    });
}
{
// 플러스와 마이너스 총괄 관리
    function updateQuantity(event, type) {
        const currentItem = event.currentTarget.closest('.item');
        const itemId = currentItem.querySelector('.id').value;
        const quantityElement = currentItem.querySelector('.quantity > .num');
        const itemPriceElement = currentItem.querySelector('.itemPrice');

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('itemId', itemId);
        formData.append('itemQuantity', quantityElement.innerText);

        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {

                return;
            }
            const response = JSON.parse(xhr.responseText);
            const updatedQuantity = response['result'];
            const basePrice = parseInt(itemPriceElement.getAttribute('data-price'), 10);
            const updatedPrice = basePrice * updatedQuantity;

            quantityElement.innerText = updatedQuantity;
            itemPriceElement.innerText = `${updatedPrice.toLocaleString()} 원`;

            calculateTotal(); // 수량 변경 후 총합 재계산

        };
        xhr.open('POST', `/cart/${type}`);
        xhr.send(formData);
    }
}

// 페이지 로드 시 총합 초기화
document.addEventListener('DOMContentLoaded', calculateTotal);

{
    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('.cancel-button').forEach(button => {
            button.addEventListener('click', (event) => {
                const currentItem = event.currentTarget.closest('.item');
                const itemId = currentItem.querySelector('.id').value;

                if (!itemId) {
                    console.error('Item ID를 찾을 수 없습니다.');
                    return;
                }

                // 다이얼로그 표시
                Dialog.showDialog({
                    onConfirm: () => {
                        // DELETE 요청 전송
                        const xhr = new XMLHttpRequest();
                        xhr.onreadystatechange = () => {
                            if (xhr.readyState === XMLHttpRequest.DONE) {
                                if (xhr.status >= 200 && xhr.status < 300) {
                                    // UI에서 항목 제거
                                    currentItem.remove();
                                    calculateTotal(); // 총합 재계산
                                } else {
                                    console.error('Failed to delete item:', xhr.responseText);
                                }
                            }
                        };
                        xhr.open('DELETE', `/cart/deleteItem?itemId=${itemId}`);
                        xhr.send();
                    },
                    onCancel: () => {
                        console.log('Deletion cancelled'); // 취소 시 로직
                    }
                });
            });
        });
    });
}

{
    $pay.onsubmit = () => {

    }
}