import styled from '@emotion/styled';
import {useSelector} from 'react-redux';
import {selectFavorite} from '../../store/favorite/favoriteSelectors';

import FavoriteItem from './FavoriteItem';

const FavoriteContainer = styled('section')({
  width: '65%',
  margin: '30px auto 10px',
  display: 'grid',
  gridTemplateColumns: '70px auto 70px 50px 50px',
  gap: '15px',
  '& button': {
    alignSelf: 'center',
    justifySelf: 'center',
  },
  '@media screen and (max-width: 1100px)': {
    width: '98%',
  },
  '@media screen and (max-width: 450px)': {
    gridTemplateColumns: '60px minmax(140px, auto) 50px 30px 30px',
    gap: '10px',
    '& svg': {
      fontSize: '1.5rem',
    },
  },
});

const FavoriteCertificates = () => {
  const favoriteCertificates = useSelector(selectFavorite);

  return (
    <FavoriteContainer>
      {favoriteCertificates.map(certificate => (
        <FavoriteItem
          key={certificate.giftCertificateId}
          certificate={certificate}
        />
      ))}
    </FavoriteContainer>
  );
};

export default FavoriteCertificates;
