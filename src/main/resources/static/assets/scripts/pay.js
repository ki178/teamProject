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

