import React from 'react';
import {Button, styled, Typography} from '@mui/material';
import FavoriteIcon from '@mui/icons-material/Favorite';
import {useFavorite} from '../favorite/hook/useFavorite';
import {useDispatch} from 'react-redux';
import {addBucketCertificate} from '../../store/bucket/bucketSlice';
import {createTagsLine} from '../../utils/createTagsLine';

const CouponCardContainer = styled('div')({
  display: 'grid',
  gridTemplateRows: '40px auto auto',
  gridTemplateColumns: '50% 50%',
  overflowWrap: 'break-word',
  rowGap: '7px',
  width: '100%',
  padding: '7px',
  boxSizing: 'border-box',
  '& h3': {
    gridColumn: '80%',
  },
});

const CouponCardContent = ({certificate}) => {
  const {name, duration, price, tags} = certificate;
  const [isFavorite, toggleFavorite] = useFavorite(certificate);
  const dispatch = useDispatch();
  const tagsLine = createTagsLine(tags);

  return (
    <CouponCardContainer>
      <Typography
        fontWeight="500"
        fontSize="15px"
        letterSpacing="-1px"
        lineHeight="16px"
        component="h3">
        {name}
      </Typography>
      <FavoriteIcon
        color={isFavorite ? 'secondary' : 'action'}
        onClick={toggleFavorite}
        sx={{fontSize: '15px', justifySelf: 'end'}}
      />
      <Typography
        fontSize={10}
        letterSpacing={-1}
        whiteSpace="nowrap"
        textOverflow="ellipsis"
        overflow="hidden">
        {tagsLine}
      </Typography>
      <Typography fontSize={10} color="var(--color-bg2)" justifySelf="end">
        Duration {duration} days
      </Typography>
      <Typography fontWeight={600}>${price}</Typography>
      <Button
        variant="outlined"
        sx={{
          padding: '2px',
          fontSize: '10px',
          textTransform: 'none',
          color: 'var(--color-bg1)',
        }}
        onClick={() => dispatch(addBucketCertificate(certificate))}>
        Add to Cart
      </Button>
    </CouponCardContainer>
  );
};

export default CouponCardContent;
