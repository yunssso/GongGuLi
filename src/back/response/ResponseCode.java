package back.response;

public enum ResponseCode {
    // COMMON ACCOUNT
    ID_MISSING(201, "아이디를 입력하세요."),
    PASSWORD_MISSING(202, "비밀번호를 입력하세요."),
    PASSWORD_CONDITIONS_NOT_MET(204, "비밀번호가 조건을 만족하지 않습니다."),
    NAME_MISSING(205, "이름을 입력하세요."),
    BIRTHDAY_MISSING(206, "생년월일을 입력하세요."),
    BIRTHDAY_CONDITIONS_NOT_MET(207, "생년월일이 조건을 만족하지 않습니다."),
    PHONE_NUMBER_MISSING(208, "전화번호를 입력하세요."),
    PHONE_NUMBER_CONDITIONS_NOT_MET(209, "전화번호가 조건을 만족하지 않습니다."),
    NO_MATCHING_USER(231, "일치하는 회원이 없습니다."),

    // SIGN UP
    SIGNUP_SUCCESS(200, "회원가입이 완료되었습니다."),
    PASSWORD_MISMATCH(203, "비밀번호가 일치하지 않습니다."),
    NICKNAME_MISSING(210, "닉네임을 입력하세요."),
    RESIDENCE_AREA_NOT_SELECTED(211, "거주 지역을 선택하세요."),
    ID_DUPLICATE(212, "중복되는 아이디가 존재합니다."),
    PHONE_NUMBER_DUPLICATE(213, "중복되는 전화번호가 존재합니다."),
    NICKNAME_DUPLICATE(214, "중복되는 닉네임이 존재합니다."),
    SIGNUP_FAILURE(215, "회원가입이 실패되었습니다."),

    // LOGIN
    LOGIN_SUCCESS(220, "로그인이 완료되었습니다."),
    PASSWORD_MISMATCH_LOGIN(221, "비밀번호가 일치하지 않습니다."),
    ID_NOT_EXIST(222, "존재하지 않은 아이디 입니다."),

    // FIND ID
    FIND_ID_SUCCESS(230, "아이디 찾기가 완료되었습니다."),

    // FIND PASSWORD
    FIND_PASSWORD_SUCCESS(240, "비밀번호 찾기가 완료되었습니다."),

    // NICKNAME CHECK
    NICKNAME_CHECK_SUCCESS(250, "해당 닉네임은 사용 가능합니다."),
    NICKNAME_CHECK_FAILURE(251, "중복되는 닉네임이 존재합니다."),

    // MODIFY USER INFO
    MODIFY_USER_INFO_SUCCESS(260, "유저 정보 수정을 완료했습니다."),
    MODIFY_USER_INFO_FAILURE(261, "유저 정보 수정을 실패했습니다."),

    // DELETE USER
    DELETE_USER_SUCCESS(270, "회원 탈퇴가 완료되었습니다."),
    DELETE_USER_FAILURE(271, "회원 탈퇴가 실패하였습니다."),

    // POST BOARD
    POST_BOARD_SUCCESS(300, "게시글 생성이 완료되었습니다."),
    POST_BOARD_FAILURE(301, "게시글 생성이 완료되었습니다."),
    TITLE_MISSING(302, "제목을 입력해주세요."),
    REGION_NOT_SELECTED(303, "지역을 선택해주세요."),
    CATEGORY_NOT_SELECTED(304, "카테고리를 선택해주세요."),
    PEOPLE_NUM_MISSING(305, "인원 수를 입력해주세요."),
    CONTENT_MISSING(306, "내용을 입력해주세요."),
    PEOPLE_NUM_OVER_LIMIT(307, "인원은 30명까지 입력 가능합니다."),
    PEOPLE_NUM_UNDER_LIMIT(308, "인원은 2명부터 입력 가능합니다."),
    PEOPLE_NUM_NOT_NUMBER(309, "인원은 숫자만 입력 가능합니다."),

    // DELETE BOARD
    DELETE_MY_BOARD_SUCCESS(1, "게시글 삭제가 완료되었습니다."),
    DELETE_MY_BOARD_FAILURE(2, "게시글 삭제에 실패하였습니다."),

    // MODIFY BOARD
    MODIFY_MY_BOARD_SUCCESS(3, "게시글 수정이 완료되었습니다."),
    MODIFY_MY_BOARD_FAILURE(4, "게시글 수정에 실패하였습니다."),

    // BOARD INFO
    BOARD_INFO_SUCCESS(400, "게시판 갱신이 완료되었습니다."),
    BOARD_INFO_FAILURE(401, "게시판 갱신에 실패하였습니다."),

    // BOARD INFO MORE
    BOARD_INFO_MORE_SUCCESS(410, "게시글 자세히 보기가 완료되었습니다."),
    BOARD_INFO_MORE_FAILURE(411, "게시글 자세히 보기가 실패하였습니다."),

    // SEARCH BOARD INFO
    SEARCH_BOARD_INFO_SUCCESS(420, "게시글 검색이 완료되었습니다."),
    SEARCH_BOARD_INFO_FAILURE(421, "게시글 검색이 실패하였습니다."),

    // JOIN CHATROOM
    JOIN_CHATROOM_SUCCESS(500, "채팅방 입장이 완료되었습니다."),
    JOIN_CHATROOM_FAILURE(501, "채팅방 입장이 실패하였습니다."),
    CHATROOM_FULL(502, "채팅방 인원이 가득 찼습니다."),

    // LEAVE CHATROOM
    LEAVE_CHATROOM_SUCCESS(510, "채팅방 퇴장이 완료되었습니다."),
    LEAVE_CHATROOM_FAILURE(511, "채팅방 퇴장이 실패하였습니다."),

    // KICK CHATROOM
    KICK_CHATROOM_SUCCESS(520, "강제퇴장이 완료되었습니다."),
    KICK_CHATROOM_FAILURE(521, "권한이 없습니다."),

    // GET CHATROOM INFO
    GET_CHATROOM_SUCCESS(530, "채팅방 정보를 가져오는데 완료하였습니다."),
    GET_CHATROOM_FAILURE(531, "채팅방 정보를 가져오는데 실패하였습니다."),

    // GET USER INFO
    GET_USERINFO_SUCCESS(600, "유저정보를 가져오는데 완료하였습니다."),
    GET_USERINFO_FAILURE(601, "유저정보를 가져오는데 실패하였습니다."),

    // GET MY BOARD INFO
    GET_MY_BOARD_INFO_SUCCESS(610, "내가 쓴 글 갱신이 완료되었습니다."),
    GET_MY_BOARD_INFO_FAILURE(611, "내가 쓴 글 갱신이 실패되었습니다."),

    // GET MY HISTORY INFO
    GET_MY_HISTORY_INFO_SUCCESS(620, "내가 참여한 거래 목록 갱신이 완료되었습니다."),
    GET_MY_HISTORY_INFO_FAILURE(621, "내가 참여한 거래 목록 갱신이 완료되었습니다."),

    // GET MY NICKNAME
    GET_UUID_NICKNAME_SUCCESS(630, "UUID에 따른 닉네임 가져오기를 완료했습니다."),
    GET_UUID_NICKNAME_FAILURE(631, "UUID에 따른 닉네임 가져오기를 실패했습니다.");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getKey() {
        return this.code;
    }

    public String getValue() {
        return this.message;
    }
}
