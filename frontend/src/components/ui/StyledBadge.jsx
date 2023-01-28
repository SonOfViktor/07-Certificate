import {styled} from '@mui/material/styles';
import {Badge} from '@mui/material';

const StyledBadge = styled(Badge)(({theme}) => ({
  '& .MuiBadge-badge': {
    right: -3,
    top: 0,
    border: `2px solid ${theme.palette.background.paper}`,
    padding: '0 0px',
    backgroundColor: 'var(--secondary)',
    color: 'var(--color-bg)',
  },
}));

export default StyledBadge;
