import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Typography,
} from '@mui/material';
import UserOrderList from './UserOrderList';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

const UserPayment = ({payment, expanded, handleAccordionExpand}) => {
  return (
    <Accordion
      key={payment.id}
      expanded={expanded === payment.id}
      onChange={handleAccordionExpand(payment)}>
      <AccordionSummary
        aria-controls={`payment${payment.id}-content`}
        id={`payment${payment.id}-header`}
        expandIcon={<ExpandMoreIcon />}>
        <Typography
          mr={3}
          fontWeight={700}>{`Payment #${payment.id}`}</Typography>
        <Typography fontWeight={500}>
          {new Date(payment.createdDate).toLocaleString()}
        </Typography>
      </AccordionSummary>
      <AccordionDetails>
        <UserOrderList payment={payment} />
      </AccordionDetails>
    </Accordion>
  );
};

export default UserPayment;
