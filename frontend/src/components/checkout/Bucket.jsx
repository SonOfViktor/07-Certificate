import {styled} from '@mui/material';
import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useNavigate} from 'react-router-dom';
import {
  selectBucket,
  selectBucketTotalPrice,
} from '../../store/bucket/bucketSelectors';
import {resetBucket} from '../../store/bucket/bucketSlice';
import {selectPaymentsMeta} from '../../store/payment/paymentSelectors';
import {
  createPayment,
  resetPaymentStatus,
} from '../../store/payment/paymentSlice';
import LoadingAlert from '../ui/LoadingAlert';
import LoadingData from '../ui/LoadingData';
import SuccessSnackbar from '../ui/SuccessSnackbar';
import TwoButtonGroup from '../ui/TwoButtonGroup';
import BucketItemList from './BucketItemList';
import BucketTotal from './BucketTotal';

const buttonsProperty = {
  marginLeft: 'auto',
  marginTop: '50px',
  width: '340px',
  '@media screen and (max-width: 750px)': {
    marginRight: 'auto',
  },
};

const BucketWrapper = styled('section')({
  width: '65%',
  margin: '30px auto 10px',
  '@media screen and (max-width: 1100px)': {
    width: '98%',
  },
});

const Bucket = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const certificates = useSelector(selectBucket);
  const totalPrice = useSelector(selectBucketTotalPrice);
  const {status, error} = useSelector(selectPaymentsMeta);

  useEffect(() => {
    if (status === 'created') {
      dispatch(resetBucket());
      dispatch(resetPaymentStatus());
    }
  });

  const handlePurchase = () => {
    const certificateIds = certificates.flatMap(certificate =>
      new Array(certificate.amount).fill(certificate.giftCertificateId)
    );

    dispatch(createPayment(certificateIds));
  };

  return (
    <BucketWrapper>
      <LoadingData status={status} />
      <LoadingAlert status={status} error={error} cleanErrorAction={resetPaymentStatus}/>
      <BucketItemList certificates={certificates} />
      <BucketTotal total={totalPrice} />
      <TwoButtonGroup
        button1="Back"
        button2="Buy"
        onClick1={() => navigate(-1)}
        onClick2={handlePurchase}
        disabled={certificates.length === 0}
        sx={buttonsProperty}
      />
      <SuccessSnackbar
        isShow={status === 'created'}
        message="Purchase was successful."
      />
    </BucketWrapper>
  );
};

export default Bucket;
