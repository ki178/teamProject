const $main = document.getElementById('main');
const $list = document.getElementById('list');
const $itemContainer = document.getElementById('item-container');
const $pay = document.getElementById('pay');


// function selectAll(selectAll) {
//     const $checkboxes = document.getElementsByName('check');
//     $checkboxes.forEach((checkbox) => {
//         checkbox.checked = selectAll.checked;
//         sendCheckboxStatus(checkbox);
//     });
//     calculateTotal();
// }

// 개별 checkbox 클릭 이벤트 추가
document.addEventListener('DOMContentLoaded', () => {
    // 체크박스 초기 상태에 따라 총합 계산
    document.querySelectorAll('.checkbox').forEach(checkbox => {
        const currentItem = checkbox.closest('.item');
        const itemId = currentItem.querySelector('.id').value;

        // 초기 체크 상태 기반으로 동작
        if (checkbox.checked){
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
    if (!currentItem){
        return;
    }
    const itemIdElement = currentItem.querySelector('.id');
    if (!itemIdElement){
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
    let hasCheckedItems = false;
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
            const $itemPrice =  parseInt($itemPriceElement.innerText.replace(/[^0-9]/g, ''), 10);
            if (!$itemId || isNaN($itemPrice)){
                console.error("Invalid itemId or itemPrice:", { $itemId, $itemPrice });
                return;
            }
            formData.append('itemId', $itemId);
            formData.append('itemPrice', $itemPrice);
            totalItemPrice += $itemPrice;
            hasCheckedItems = true;
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
            updateUI(response['totalPrice']);

            function updateUI(totalPrice) {
                if (totalPrice > 0) {
                    // 체크된 항목이 있을 때
                    const priceText = totalPrice > 0 ? `${totalPrice.toLocaleString()} 원` : `0 원`;
                    const buttonText = totalPrice > 0 ? `${totalPrice.toLocaleString()} 원 주문하기` : `상품을 선택해주세요`;
                    // 상품 금액 영역 업데이트
                    document.querySelector('.pay > .price').innerText = priceText;
                    // 결제 예정 금액 업데이트
                    document.querySelector('.total-pay > .price').innerText = priceText;
                    // 주문하기 버튼 업데이트
                    document.querySelector('.pay-button').innerText = buttonText;
                    //
                    document.querySelector('.items-price > ._text').innerText = `상품 ${totalPrice.toLocaleString()} 원 + 배송비 무료`;
                    document.querySelector('.total-price').innerText = priceText;
                } else {
                    // 체크된 항목이 없을 때
                    const priceText = totalPrice > 0 ? `${totalPrice.toLocaleString()} 원` : `0 원`;
                    const buttonText = totalPrice > 0 ? `${totalPrice.toLocaleString()} 원 주문하기` : `상품을 선택해주세요`;
                    // 상품 금액 영역 업데이트
                    document.querySelector('.pay > .price').innerText = priceText;
                    // 결제 예정 금액 업데이트
                    document.querySelector('.total-pay > .price').innerText = priceText;
                    // 주문하기 버튼 업데이트
                    document.querySelector('.pay-button').innerText = buttonText;
                    //
                    document.querySelector('.items-price > ._text').innerText = `상품 0 원 + 배송비 무료`;
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
    // const selectAllCheckbox = document.getElementById('selectAll');
    // if (selectAllCheckbox) {
    //     selectAllCheckbox.addEventListener('change', () => selectAll(selectAllCheckbox));
    // }

    calculateTotal(); // 페이지 로드 시 총합 계산
});



// { 플러스와 마이너스 개별
//     const $payPrice = $pay.querySelector(':scope > .price');
//     const $itemPriceBefore = document.querySelector('.itemPrice');
//     const $quantity = document.querySelectorAll('.quantity');
//     $quantity.forEach(product => {
//         const minusButton = product.querySelector('.minus');
//         const plusButton = product.querySelector('.plus');
//
//         let previousResult = null; // 이전 결과를 저장하는 변수
//         let repeatCount = 0; // 연속된 결과의 횟수를 카운트
//
//         plusButton.addEventListener('click', (event) => {
//
//             // 현재 클릭한 버튼의 상위 .item 요소를 찾음
//             const currentItem = event.currentTarget.closest('.item');
//
//             // 현재 .item 요소 내에서 필요한 값 가져오기
//             const $quantity = currentItem.querySelector('.quantity > .num');
//             const $itemPrice = currentItem.querySelector('.itemPrice');
//             const $itemId = currentItem.querySelector('.id');
//
//             const xhr = new XMLHttpRequest();
//             const url = new URL(location.href);
//             url.pathname = '/cart/plus'
//             const formData = new FormData();
//             formData.append('itemQuantity', $quantity.textContent);
//             formData.append('itemId', $itemId.value);
//             xhr.onreadystatechange = () => {
//                 if (xhr.readyState !== XMLHttpRequest.DONE) {
//                     return;
//                 }
//                 if (xhr.status < 200 || xhr.status >= 300) {
//                     alert(`오류 : ${xhr.status}`);
//                     return;
//                 }
//                 const response = JSON.parse(xhr.responseText);
//                 const currentResult = response['result']; // 현재 서버 응답 결과
//                 // 결과가 두 번 연속 같은 경우 알림
//                 if (currentResult === previousResult) {
//                     repeatCount++;
//                 } else {
//                     repeatCount = 1; // 결과가 다르면 카운트 초기화
//                 }
//
//                 if (repeatCount === 2) {
//                     alert("제품의 최대 수량은 50개 입니다.");
//                     repeatCount = 0; // 카운트 초기화
//                 }
//                 previousResult = currentResult; // 이전 결과 업데이트
//
//                 // 기존 로직: 화면 업데이트
//                 $quantity.innerText = currentResult;
//
//
//                 const basePrice = parseInt($itemPrice.getAttribute('data-price'), 10);
//
//                 const newPrice = basePrice * currentResult;
//                 $itemPrice.innerText = `${newPrice.toLocaleString()} 원`;
//
//             };
//
//
//             xhr.open('POST', url.toString());
//             xhr.send(formData);
//         });
//
//
//         minusButton.addEventListener('click', (event) => {
//
//             // 현재 클릭한 버튼의 상위 .item 요소를 찾음
//             const currentItem = event.currentTarget.closest('.item');
//
//             const $quantity = currentItem.querySelector('.quantity > .num');
//             const $itemPrice = currentItem.querySelector('.itemPrice');
//             const $itemId = currentItem.querySelector('.id');
//
//                 const xhr = new XMLHttpRequest();
//                 const url = new URL(location.href);
//                 url.pathname = '/cart/minus'
//                 const formData = new FormData();
//                 formData.append('itemQuantity', $quantity.textContent);
//                 formData.append('itemId', $itemId.value);
//                 xhr.onreadystatechange = () => {
//                     if (xhr.readyState !== XMLHttpRequest.DONE) {
//                       return;
//                       }
//                       if (xhr.status < 200 || xhr.status >= 300) {
//
//                       return;
//                       }
//                     const response = JSON.parse(xhr.responseText);
//                     const currentResult = response['result']; // 현재 서버 응답 결과
//                     // 결과가 두 번 연속 같은 경우 알림
//                     if (currentResult === previousResult) {
//                         repeatCount++;
//                     } else {
//                         repeatCount = 1; // 결과가 다르면 카운트 초기화
//                     }
//
//                     if (repeatCount === 2) {
//                         alert("수량은 최소 1개 이상 입니다.");
//                         repeatCount = 0; // 카운트 초기화
//                     }
//                     previousResult = currentResult; // 이전 결과 업데이트
//
//                     // 기존 로직: 화면 업데이트
//                     $quantity.innerText = currentResult;
//
//
//                     const basePrice = parseInt($itemPrice.getAttribute('data-price'), 10);
//
//                     const newPrice = basePrice * currentResult;
//                     $itemPrice.innerText = `${newPrice.toLocaleString()} 원`;
//                 };
//                 xhr.open('POST', url.toString());
//                 xhr.send(formData);
//
//
//         })
//     })
// }

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

// 페이지 로드 시 총합 초기화
document.addEventListener('DOMContentLoaded', calculateTotal);

// {
//     // 체크박스 상태 변경
//     document.querySelectorAll('.checkbox').forEach(checkbox => {
//        checkbox.addEventListener('change', (event) => {
//           const currentItem = event.currentTarget.closest('.item');
//           const $itemId = currentItem.querySelector('.id');
//           const $isChecked = event.currentTarget.checked ? 1 : 0;
//
//           const xhr = new XMLHttpRequest();
//           const url = new URL(location.href);
//           url.pathname = '/cart/updateCheck';
//           const formData = new FormData();
//           formData.append('itemId', $itemId.value);
//           formData.append('isChecked', $isChecked);
//           xhr.onreadystatechange = () => {
//               if (xhr.readyState !== XMLHttpRequest.DONE) {
//                 return;
//                 }
//                 if (xhr.status < 200 || xhr.status >= 300) {
//
//                 return;
//                 }
//           };
//           xhr.open('POST', url.toString());
//           xhr.send(formData);
//        });
//     });
//     // 체크된 항목 총합 계산
//     function calculateTotal(){
//         const formData = new FormData();
//
//         document.querySelectorAll('.item').forEach(item => {
//             const checkbox = item.querySelector('.checkbox');
//
//             if (checkbox.checked) {
//                 const itemId = item.querySelector('.id').value;
//                 const itemPrice = parseInt(item.querySelector('.itemPrice').innerText.replace(/[^0-9]/g, ''), 10);
//
//                 formData.append('items', JSON.stringify({itemId, itemPrice}));
//             }
//         });
//
//        const xhr = new XMLHttpRequest();
//        const url = new URL(location.href);
//        url.pathname = '/cart/totalPrice';
//
//        xhr.onreadystatechange = () => {
//            if (xhr.readyState !== XMLHttpRequest.DONE) {
//              return;
//              }
//              if (xhr.status < 200 || xhr.status >= 300) {
//
//              return;
//              }
//            const response = JSON.parse(xhr.responseText);
//            const totalPrice = response.totalPrice;
//
//            // 화면에 총합 업데이트
//            const totalPriceText = `${totalPrice.toLocaleString()} 원`;
//            document.querySelector('.items-price > ._text').innerText = `상품 "${totalPriceText}" + 배송비 무료`;
//            document.querySelector('.total-price').innerText = `"${totalPriceText}"`;
//        };
//        xhr.open('POST', url.toString());
//        xhr.send(formData);
//     }
//     // 체크박스 상태 변경 시 총합 재계산
//     document.querySelectorAll('.checkbox').forEach(checkbox => {
//         checkbox.addEventListener('change', calculateTotal);
//     });
//
//     // 페이지 로드 시 초기 총합 계산
//     document.addEventListener('DOMContentLoaded', calculateTotal);
//
//
// }

{
    $pay.onsubmit = () => {

    }
}