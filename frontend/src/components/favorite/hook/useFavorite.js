import {useDispatch, useSelector} from 'react-redux';
import {
  addFavoriteCertificate,
  deleteFavoriteCertificate,
} from '../../../store/favorite/favoriteSlice';
import {selectIsFavorite} from '../../../store/favorite/favoriteSelectors';

export const useFavorite = certificate => {
  const dispatch = useDispatch();
  const isFavorite = useSelector(state =>
    selectIsFavorite(state, certificate.giftCertificateId)
  );

  const toggleFavorite = () => {
    if (isFavorite) {
      dispatch(deleteFavoriteCertificate(certificate.giftCertificateId));
    } else {
      dispatch(addFavoriteCertificate(certificate));
    }
  };

  return [isFavorite, toggleFavorite];
};
