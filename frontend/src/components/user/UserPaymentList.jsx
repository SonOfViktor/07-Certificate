import {Pagination, Stack, Typography, useMediaQuery} from '@mui/material';
import {useSelector} from 'react-redux';
import {selectPayments} from '../../store/payment/paymentSelectors';
import LoadingAlert from '../ui/LoadingAlert';
import LoadingData from '../ui/LoadingData';
import {usePayments} from './hooks/usePayments';
import UserPayment from './UserPayment';
import {resetPaymentStatus} from "../../store/payment/paymentSlice";

const UserPaymentList = ({user}) => {
  const width600 = useMediaQuery('(max-width:600px)');
  const {status, payments, error, page} = useSelector(selectPayments);
  const {expanded, pageNumber, handleChangePage, handleAccordionExpand} =
    usePayments(user);

  return (
    <>
      <Typography mx={2} mb={1} variant="h6" component="h2">
        Payments
      </Typography>
      <LoadingData status={status} />
      <LoadingAlert status={status} error={error} cleanErrorAction={resetPaymentStatus}/>
      <Stack mx={2} mb={2} maxWidth={width600 ? '100%' : '80%'}>
        {payments.map(payment => (
          <UserPayment
            key={payment.id}
            payment={payment}
            expanded={expanded}
            handleAccordionExpand={handleAccordionExpand}
          />
        ))}
        {page.totalPages > 1 && (
          <Pagination
            sx={{margin: '10px auto'}}
            count={page.totalPages}
            color="primary"
            page={pageNumber}
            siblingCount={2}
            onChange={handleChangePage}
          />
        )}
      </Stack>
    </>
  );
};

export default UserPaymentList;
