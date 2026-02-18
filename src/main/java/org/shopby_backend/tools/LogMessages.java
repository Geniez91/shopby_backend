package org.shopby_backend.tools;

public final class LogMessages {
    private LogMessages() {
    }
    /* =========================
       ARTICLE
       ========================= */
        public static final String ARTICLE_ALREADY_EXISTS="L'article existe deja : articleName={}";
        public static final String BRAND_NOT_FOUND="Aucune marque ne correspond à l'id de la marque : idBrand={}";
        public static final String TYPE_ARTICLE_NOT_FOUND_BY_ID="Aucun type article n'existe avec l'id : idType={}";
        public static final String TYPE_ARTICLE_NOT_FOUND_BY_PARENT_ID="Le parent est introuvable avec l'id : idParent={}";
        public static final String ARTICLE_NOT_FOUND="Aucun article ne correspond à l'id de l'article : idArticle={}";
        public static final String BRAND_ALREADY_EXISTS="Le marque existe deja : brandName {}";
        public static final String USERS_NOT_FOUND_BY_USER_ID="L'utilisateur n'existe pas avec idUser={}";
        public static final String USERS_NOT_FOUND_BY_USER_EMAIL="L'utilisateur n'existe pas avec l'email :{}";
        public static final String STATUS_NOT_FOUND_BY_ID="Aucun status n'existe avec l'id={}";
        public static final String STATUS_NOT_FOUND_BY_ORDER_STATUS="Aucun status ne correspond au status={}";
        public static final String ORDER_NOT_FOUND_BY_DATE_AND_STATUS_ORDER="Aucun commande ne correspond au filtre date order : {} et le status order : {}";
        public static final String ORDER_NOT_FOUND_BY_ID="Le orderId ne correspondent pas un commande spécifique : {}";
        public static final String ORDER_NOT_FOUND_BY_USER_ID="Aucune commande ne correspond au userId {}";
        public static final String ORDER_ITEM_NOT_FOUND_BY_ORDER_ID="Aucun article ne correspond à la commande {}";
        public static final String STATUS_ALREADY_EXISTS="Le status existe deja avec le libelle {}";
        public static final String TYPE_ARTICLE_ALREADY_EXISTS="Le type d'article existe deja avec le libelle {}";
        public static final String USERS_ALREADY_EXISTS="Vos identifiants existe deja avec email : {}";
        public static final String VALIDATION_EXPIRED="Le code d'utilisateur a expiré le {}";
        public static final String VALIDATION_NOT_FOUND_BY_CODE="Le code d'action n'est pas valide avec le code {}";
        public static final String WISHLIST_NOT_FOUND_BY_ID="L'id de la liste d'envie ne correspond a aucun liste avec l'id {}";
        public static final String ARTICLE_ID_ALREADY_EXISTS_IN_WISHLIST_ID="L'article est deja dans votre liste d'envie avec l'id wishlist {} et l'id article : {}";
        public static final String WISHLIST_ITEM_NOT_FOUND_BY_WISHLIST_ID_AND_ARTICLE_ID="L'id de liste d'envie et de l'article ne correpond a aucun article d'une liste d'envie avec idWishlist {} et idArticle : {}";
        public static final String COMMENT_ALREADY_EXISTS="Le commentaire existe deja avec idArticle : {} et idUser : {}";
        public static final String COMMENT_NOT_FOUND="Aucune commentaire ne correspond à l'id du commentaire : {}";
        public static final String COMMENT_LIKE_ALREADY_EXISTS="Le commentaire est deja like avec idComment : {} , idUser : {}";
        public static final String COMMENT_LIKE_NOT_FOUND="Aucune like sur commentaire ne correspond à l'id du commentaire : {}";
    }

