class Dialog {
    static showDialog({
                          message = "삭제하시겠습니까?",
                          onConfirm = () => {},
                          onCancel = () => {}
                      }) {
        // 다이얼로그 오버레이 생성
        const dialogCover = document.createElement('div');
        dialogCover.className = '---dialog-cover'; // 흐릿한 배경 클래스

        // 다이얼로그 컨테이너 생성
        const dialog = document.createElement('div');
        dialog.className = '--dialog'; // 다이얼로그 컨테이너 클래스

        // 다이얼로그 제목 및 버튼 컨테이너 생성
        const title = document.createElement('div');
        title.className = '_title';

        const question = document.createElement('p');
        question.className = '_question';
        question.textContent = message;

        // 버튼 컨테이너를 제목 안으로 이동
        const buttonContainer = document.createElement('div');
        buttonContainer.className = '_button-container';

        // 취소 버튼 생성
        const cancelButton = document.createElement('button');
        cancelButton.className = 'cancel _button';
        cancelButton.setAttribute('aria-label', 'cancel-button');
        cancelButton.textContent = '취소';
        cancelButton.onclick = () => {
            document.body.removeChild(dialogCover); // 다이얼로그 제거
            if (typeof onCancel === 'function') {
                onCancel();
            }
        };

        // 확인 버튼 생성
        const confirmButton = document.createElement('button');
        confirmButton.className = 'confirm _button';
        confirmButton.setAttribute('aria-label', 'confirm-button');
        confirmButton.textContent = '확인';
        confirmButton.onclick = () => {
            document.body.removeChild(dialogCover); // 다이얼로그 제거
            if (typeof onConfirm === 'function') {
                onConfirm();
            }
        };

        // 버튼 컨테이너에 버튼 추가
        buttonContainer.appendChild(cancelButton);
        buttonContainer.appendChild(confirmButton);

        // 제목 안에 질문과 버튼 컨테이너 추가
        title.appendChild(question);
        title.appendChild(buttonContainer);

        // 다이얼로그에 제목 추가
        dialog.appendChild(title);

        // 오버레이에 다이얼로그 추가
        dialogCover.appendChild(dialog);

        // 문서에 추가
        document.body.appendChild(dialogCover);
    }
}
