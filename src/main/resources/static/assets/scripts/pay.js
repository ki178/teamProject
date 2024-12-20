document.addEventListener("DOMContentLoaded", () => {
    const inputToggleButton = document.querySelector(".input-button"); // 입력 버튼 선택
    const deliveryRequestSection = document.querySelector("label.test"); // 숨겨진 입력창 선택

    inputToggleButton.addEventListener("click", () => {
        // visible 클래스 추가/제거
        if (deliveryRequestSection.classList.contains("visible")) {
            deliveryRequestSection.classList.remove("visible"); // 숨김
        } else {
            deliveryRequestSection.classList.add("visible"); // 표시
        }
    });
});