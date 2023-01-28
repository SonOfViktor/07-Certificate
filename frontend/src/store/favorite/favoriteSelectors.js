export const selectFavorite = state => state.favorite;
export const selectFavoriteLength = state => state.favorite.length;
export const selectIsFavorite = (state, id) =>
  state.favorite.some(certificate => certificate.giftCertificateId === id);
