import {styled} from '@mui/material';
import Coupon from './Coupon';
import {useEndlessLoadingCertificates} from './hooks/useEndlessLoadingCertificates';

const CouponContainer = styled('section')({
  display: 'grid',
  padding: '30px 10px',
  gridTemplateColumns: 'repeat(auto-fill, 200px)',
  gap: 15,
  justifyContent: 'center',
});

const CouponList = () => {
  const [status, lastElement, certificateList] =
    useEndlessLoadingCertificates();

  return (
    <CouponContainer>
      {certificateList.map(certificate => (
        <Coupon key={certificate.giftCertificateId} certificate={certificate} />
      ))}
      {status !== 'loading' && (
        <div ref={lastElement} data-testid="last-element" />
      )}
    </CouponContainer>
  );
};

export default CouponList;
