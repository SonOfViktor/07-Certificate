import React from 'react';
import BucketContainer from './BucketContainer';
import BucketItem from './BucketItem';

const BucketItemList = ({certificates}) => {
  return (
    <BucketContainer>
      {certificates.map(certificate => (
        <BucketItem
          key={certificate.giftCertificateId}
          certificate={certificate}
        />
      ))}
    </BucketContainer>
  );
};

export default BucketItemList;
