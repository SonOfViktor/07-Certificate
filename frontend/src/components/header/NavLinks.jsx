import React from 'react';
import {useNavigate} from 'react-router-dom';
import StyledBadge from '../ui/StyledBadge';
import FavoriteIcon from '@mui/icons-material/Favorite';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import {Stack, styled} from '@mui/material';
import {useDispatch, useSelector} from 'react-redux';
import {logout} from '../../store/user/userSlice';
import UserAvatar from './UserAvatar';
import LogoutIcon from '@mui/icons-material/Logout';
import {selectFavoriteLength} from '../../store/favorite/favoriteSelectors';
import LoginIcon from '@mui/icons-material/Login';
import HoverIconButton from '../ui/HoverIconButton';
import {selectBucketLength} from '../../store/bucket/bucketSelectors';
import {selectUser} from '../../store/user/userSelectors';

const StyledStack = styled(Stack)({
  alignItems: 'center',
  gap: '5px',
  '@media screen and (max-width: 600px)': {
    '& svg': {
      fontSize: '1.5rem',
    },
  },
});

const NavLinks = () => {
  const user = useSelector(selectUser);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const favoriteCertificateAmount = useSelector(selectFavoriteLength);
  const bucketCertificateAmount = useSelector(selectBucketLength);

  return (
    <StyledStack direction="row">
      <HoverIconButton onClick={() => navigate('/favorite')}>
        <StyledBadge badgeContent={favoriteCertificateAmount} color="secondary">
          <FavoriteIcon />
        </StyledBadge>
      </HoverIconButton>
      <HoverIconButton onClick={() => navigate('/checkout')}>
        <StyledBadge badgeContent={bucketCertificateAmount} color="secondary">
          <ShoppingCartIcon />
        </StyledBadge>
      </HoverIconButton>

      {user ? (
        <>
          <UserAvatar
            firstName={user.firstName}
            lastName={user.lastName}
            onClick={() => navigate('/user')}
          />
          <HoverIconButton onClick={() => dispatch(logout())}>
            <LogoutIcon />
          </HoverIconButton>
        </>
      ) : (
        <>
          <HoverIconButton onClick={() => navigate('/login')}>
            <LoginIcon />
          </HoverIconButton>
        </>
      )}
    </StyledStack>
  );
};

export default NavLinks;
